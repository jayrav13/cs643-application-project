# Import
import json
import requests
import sys
from datetime import datetime
import time

# Open raw data file.
f = open("/home/ubuntu/application-project/data/raw/other-American_B01362.csv", "r")
data = f.readlines()

result = data[0]

# Prepare output files.
g = open("/home/ubuntu/application-project/data/result/American.csv", "a+")
h = open("/home/ubuntu/application-project/data/result/American-Errors", "a+")

# Set up failsafe counter.
counter = 0

# 
for i in range(1, len(data)):
	element = data[i].split(",")
	params = {
		"address": element[2].replace("Cortlandt", "NY"),
		"key": "AIzaSyDFjkxEruTRRcSwZsWA7mnSVYECRA_D-q0"
	}
	response = requests.get("https://maps.googleapis.com/maps/api/geocode/json", params=params)

	result = response.json()

	if len(result["results"]) == 0:
		h.write(data[i])
		print "ERROR: " + element[2]
		print response.text
		counter += 1

		if counter > 25:
			sys.exit()

		continue

	datetime_object = datetime.strptime(element[0] + " " + element[1], '%m/%d/%Y %I:%M:%S %p')

	g.write( "\"" + datetime_object.strftime('%m/%d/%Y %H:%M:%S') + "\"," + str(result["results"][0]["geometry"]["location"]["lat"]) + "," + str(result["results"][0]["geometry"]["location"]["lng"]) + ",\"B01362\",\"American\",\"FHV\"\n")

	print "SUCCESS: " + element[2]
	time.sleep(0.05)
	counter = 0

g.close()
h.close()


