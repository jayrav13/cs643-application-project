import json
import requests
import sys
from datetime import datetime
import time

f = open("../inputs/other-Carmel_B00256.csv", "r")
data = f.readlines()

result = data[0]

g = open("../outputs/Carmel.csv", "a+")
h = open("../outputs/Carmel-Errors", "a+")

for i in range(1, len(data)):
	element = data[i].split(",")
	params = {
		"address": element[2],
		"benchmark": "9",
		"format": "json"
	}
	response = requests.get("https://geocoding.geo.census.gov/geocoder/locations/onelineaddress", params=params)

	result = response.json()

	if len(result["result"]) == 0:
		h.write(data[i])
		print "ERROR: " + element[2]
		continue

	datetime_object = datetime.strptime(element[0] + " " + element[1], '%m/%d/%Y %H:%M')

	g.write( "\"" + datetime_object.strftime('%m/%d/%Y %H:%M:%S') + "\"," + str(result["result"]["addressMatches"][0]["coordinates"]["y"]) + "," + str(result["result"]["addressMatches"][0]["coordinates"]["x"]) + ",\"B00256\",\"Carmel\",\"FHV\"\n")

	print "SUCCESS: " + element[2]

g.close()
h.close()
