import json
import requests
import sys
from datetime import datetime
import time

f = open("../inputs/other-Prestige_B01338.csv", "r")
data = f.readlines()

result = data[0]

g = open("../outputs/other-Prestige_B01338.csv", "a+")
h = open("../outputs/Prestige-Errors", "a+")

counter = 0

for i in range(1, len(data)):
        element = [x.strip() for x in data[i].split(",")]

        address = element[2] + " NY"

        if "EWR" in address:
                address = "Newark Airport Newark NJ"
        if "JFK" in address:
                address = "John F Kennedy Airport Queens NY"
        if "LGA" in address:
                address = "LaGuardia Airport Queens NY"

        params = {
                "address": address,
                "sensor": "false"
        }

        response = requests.get("http://34.203.232.216/maps/api/geocode/json", params=params)

        try:
                result = response.json()
        except Exception as e:
                result = None

        if result is None or len(result["results"]) == 0:
                h.write(data[i])
                print "ERROR: " + address
                counter += 1
                if counter > 25:
                        sys.exit()
                continue

        datetime_object = datetime.strptime(element[0] + " " + element[1], '%m/%d/%Y %I:%M:%S %p')

        g.write( "\"" + datetime_object.strftime('%m/%d/%Y %H:%M:%S') + "\"," + str(result["results"][0]["geometry"]["location"]["lat"]) + "," + str(result["results"][0]["geometry"]["location"]["lng"]) + ",\"B01338\",\"Prestige\",\"FHV\"\n")

        print "SUCCESS: " + address
        time.sleep(0.05)
        counter = 0

g.close()
h.close()
