"""Vehicles Routing Problem (VRP)"""

import distance_matrix
import firebase
from collections import defaultdict
def create_data_model():
    """Stores the data for the problem. Data does not have to be calculated at runtime"""
    data = {}
    data['distance_matrix'] = distance_matrix.dist_matrix()
    driver_index = firebase.firebase()[1]
    data['num_vehicles'] = len(driver_index) #Entered by reading database

    # Driver address indices should be the start indices
    data['starts'] = driver_index

    # Zero should be the Citrix address index
    data['ends'] = [0]*len(driver_index)

    # Vehicle capacities can be added through app
    data['vehicle_capacities'] = [2]*len(driver_index)
    print(data)
    return data

def print_solution(data, manager, routing, assignment):
    route_dict = defaultdict(list)
    flag = 0
    dropped_nodes = 'Dropped nodes:'
    for node in range(routing.Size()):
        if routing.IsStart(node) or routing.IsEnd(node):
            continue
        if assignment.Value(routing.NextVar(node)) == node:
            dropped_nodes += ' {}'.format(manager.IndexToNode(node))
    print(dropped_nodes)
    """Prints assignment on console."""
    total_distance = 0
    total_load = 0
    for vehicle_id in range(data['num_vehicles']):
        index = routing.Start(vehicle_id)
        plan_output = 'Route for vehicle {}:\n'.format(vehicle_id)
        route_distance = 0
        route_load = 0
        while not routing.IsEnd(index):
            node_index = manager.IndexToNode(index)
            route_load += 1
            plan_output += ' {0} Load({1}) -> '.format(node_index, route_load)
            if flag == 0:
                previous_node_index = node_index
            if flag > 0:
                route_dict[previous_node_index].append(node_index)
            previous_index = index
            index = assignment.Value(routing.NextVar(index))
            route_distance += routing.GetArcCostForVehicle(previous_index, index, vehicle_id)
            flag += 1
        flag = 0
        plan_output += ' {0} Load({1})\n'.format(manager.IndexToNode(index), route_load)
        plan_output += 'Distance of the route: {}m\n'.format(route_distance)
        plan_output += 'Load of the route: {}\n'.format(route_load)
        print(plan_output)
        total_distance += route_distance
        total_load += route_load
    print('Total distance of all routes: {}m'.format(total_distance))
    print('Total load of all routes: {}'.format(total_load))
    return route_dict







