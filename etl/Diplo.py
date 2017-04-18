import json
import requests
import sys
from datetime import datetime
import time

f = open("../inputs/other-Diplo_B01196.csv", "r")
data = f.readlines()

result = data[0]

g = open("../outputs/Diplo.csv", "a+")
h = open("../outputs/Diplo-Errors", "a+")

for i in range(1, len(data)):
	element = data[i].split(",")
	params = {
		"address": element[2],
		"key": ""
	}
	response = requests.get("https://maps.googleapis.com/maps/api/geocode/json", params=params)

	result = response.json()

	if len(result["results"]) == 0:
		h.write(data[i])
		print "ERROR: " + element[2]
		continue

	datetime_object = datetime.strptime(element[0] + " " + element[1], '%m/%d/%Y %I:%M:%S %p')

	g.write( "\"" + datetime_object.strftime('%m/%d/%Y %H:%M:%S') + "\"," + str(result["results"][0]["geometry"]["location"]["lat"]) + "," + str(result["results"][0]["geometry"]["location"]["lng"]) + ",\"B01196\",\"Diplo\",\"FHV\"\n")

	print "SUCCESS: " + element[2]

g.close()
h.close()


