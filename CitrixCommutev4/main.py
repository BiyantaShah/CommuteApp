from citrix_commute import *
from ortools.constraint_solver import routing_enums_pb2
from ortools.constraint_solver import pywrapcp
import firebase

def main():
    """Solve the CVRP problem."""
    # Instantiate the data problem.
    data = create_data_model()

    # Create the routing index manager.
    manager = pywrapcp.RoutingIndexManager(
        len(data['distance_matrix']), data['num_vehicles'], data['starts'], data['ends'])

    # Create Routing Model.
    routing = pywrapcp.RoutingModel(manager)

    '''Create and register a transit callback. The distance matrix records the direct distances between the nodes. The distance callback can do some extra manipulations on top of what's recorded
    in the distance matrix'''

    def distance_callback(from_index, to_index):
        """Returns the distance between the two nodes. And also sets the cost of traveling between nodes (by routing.SetArcCostEvaluatorOfAllVehicles). and passes the cost to the solver"""
        # Convert from routing variable Index to distance matrix NodeIndex.
        from_node = manager.IndexToNode(from_index)
        to_node = manager.IndexToNode(to_index)
        return data['distance_matrix'][from_node][to_node]

    transit_callback_index = routing.RegisterTransitCallback(distance_callback)

    # Define cost of each arc(edge) in the graph using the distance callback
    routing.SetArcCostEvaluatorOfAllVehicles(transit_callback_index)

    dimension_name2 = 'Stops'
    car_capacity_callback_index = routing.RegisterUnaryTransitCallback(lambda index: 1)
    '''AddDimension applies dimension to all vehicles,
        AddDimensionWithVehicleCapacity can take a vector of capacities and apply them to corresponding vehicles)'''
    routing.AddDimension(
        car_capacity_callback_index,
        0,  # null capacity slack
        3,  # vehicle maximum number of stops
        True,  # start cumul to zero
        'Stops')

    # allow to drop nodes
    penalty = 15000
    for node in range(1, len(data['distance_matrix'])):
        routing.AddDisjunction([manager.NodeToIndex(node)], penalty)

    # Setting first solution heuristic.
    search_parameters = pywrapcp.DefaultRoutingSearchParameters()  # search strategy
    search_parameters.first_solution_strategy = (
        routing_enums_pb2.FirstSolutionStrategy.PATH_CHEAPEST_ARC)

    # Solve the problem.
    solution = routing.SolveWithParameters(search_parameters)
    if not solution:
        print("No solution")
    # Print solution on console.
    if solution:
        route_dict = print_solution(data, manager, routing, solution)
        driver_map = firebase.route(route_dict)
        driver_map1 = firebase.stringify(driver_map)
        firebase.add_to_db(driver_map1)
        rider_map = firebase.create_rider_map(driver_map)
        firebase.add_to_db_rider(rider_map)




if __name__ == '__main__':
    main()