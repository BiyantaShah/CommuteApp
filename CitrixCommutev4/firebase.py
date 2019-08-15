import firebase_admin
from firebase_admin import credentials

from firebase_admin import db

cred = credentials.Certificate("Users/arsheenkhatib/Desktop/citrix/hackathon_2019/carpool2019-280e4-firebase-adminsdk-rkz1p-59eecb810c.json")
firebase_admin.initialize_app(cred, {'databaseURL' : "https://carpool2019-280e4.firebaseio.com/"})
root = db.reference("Users")
users = root.get()


def firebase():
  driver_indexes = []
  addresses = []
  end_address = "851 W Cypress Creek Rd, Fort Lauderdale, FL 33309"
  addresses.append(end_address)
  index = 0
  for user in users.keys():
      addresses.append(users[user]['userAddress'])
      index+=1
      if users[user]['type'] == 'd':
          driver_indexes.append(index)
  address = urlizer(addresses)
  return (address, driver_indexes)

def route(route_dict):
    user_indexes = {}
    driver_map = {}
    index = 0
    for user in users.keys():
        index += 1
        user_indexes[index] = user
    for i in user_indexes:
        if i in route_dict:
            print(i)
            driver_name = user_indexes[i]
            rider = []
            for j in route_dict[i]:
                rider.append(user_indexes[j])

            driver_map[driver_name] = rider
    return driver_map

def stringify(driver_map):
    driver_map_string = {}
    for i in driver_map:
        driver_map_string[i]=":".join(driver_map[i])
    return driver_map_string

def add_to_db(driver_map):
    for i in driver_map:
        root.child(i).update({'results':driver_map[i]})


def create_rider_map(driver_map):
    rider_map = {}
    for dr in driver_map:
        for i in driver_map[dr]:
            rider_map[i] = dr
    return rider_map

def add_to_db_rider(rider_map):
    for i in rider_map:
        root.child(i).update({'results':rider_map[i]})


def urlizer(address_list):
  nl = []
  for i in address_list:
    j = i.replace(" ","+")
    nl.append(j)
  return nl
      # print(user, "  :  ", users[user]['type'])
  # j = 0
  # for i in addresses:
  #     print(j, i, "\n")
  #     j+=1
  #
  # print(driver_indexes)



