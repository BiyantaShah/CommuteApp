# import pyrebase
# config = {
#   "apiKey": "AIzaSyAqOHOYe8hB2fjV_gyq7t9jh3WXS8dB0-Y",
#   "authDomain": "carpool2019-280e4.firebaseapp.com",
#   "databaseURL": "https://carpool2019-280e4.firebaseio.com/",
#   "storageBucket": "carpool2019-280e4.appspot.com",
#   "serviceAccount": "Users/arsheenkhatib/Desktop/citrix/hackathon_2019/carpool2019-280e4-firebase-adminsdk-rkz1p-59eecb810c.json"
# }
# firebase = pyrebase.initialize_app(config)
#
#
# auth = firebase.auth()
#
# #authenticate a user
# user = auth.sign_in_with_email_and_password("poojaforproject599@gmail.com", "forproject599")
#
import firebase_admin
from firebase_admin import credentials

from firebase_admin import db

cred = credentials.Certificate("Users/arsheenkhatib/Desktop/citrix/hackathon_2019/carpool2019-280e4-firebase-adminsdk-rkz1p-59eecb810c.json")
firebase_admin.initialize_app(cred, {'databaseURL' : "https://carpool2019-280e4.firebaseio.com/"})
root = db.reference("Users")
users = root.get()
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
    # print(user, "  :  ", users[user]['type'])
j = 0
for i in addresses:
    print(j, i, "\n")
    j+=1

print(driver_indexes)




# for i in users.get():
#     print(type(i))
# new_user = users.child('Mary').set({"count":12121, "ahdsd":None})

# for i in users.get():
#     print(i["count"]+"        \n")