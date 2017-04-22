import json
import requests
import sys
from datetime import datetime
import time

f = open("/home/ubuntu/application-project/data/raw/other-Dial7_B00887.csv", "r")
data = f.readlines()

result = data[0]

g = open("/home/ubuntu/application-project/data/result/Dial7.csv", "a+")
h = open("/home/ubuntu/application-project/data/result/Dial7-Errors", "a+")

counter = 0

for i in range(150000, len(data)):
	element = [x.strip() for x in data[i].split(",")]

	address = element[4] + " " + element[5] + " " + element[3] + " " + element[2]

	if "EWR" in address:
		address = "Newark Airport Newark NJ"
	if "JFK" in address:
		address = "John F Kennedy Airport Queens NY"
	if "LGA" in address:
		address = "LaGuardia Airport Queens NY"

	address = address.replace("MANHATTAN", "NEW YORK")

	params = {
		"address": address,
#		"key": "AIzaSyDFjkxEruTRRcSwZsWA7mnSVYECRA_D-q0",
		"sensor": "false"
	}

	response = requests.get("http://52.37.239.39/maps/api/geocode/json", params=params)

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

	datetime_object = datetime.strptime(element[0] + " " + element[1], '%Y.%m.%d %H:%M')

	g.write( "\"" + datetime_object.strftime('%m/%d/%Y %H:%M:%S') + "\"," + str(result["results"][0]["geometry"]["location"]["lat"]) + "," + str(result["results"][0]["geometry"]["location"]["lng"]) + ",\"B00887\",\"Dial7\",\"FHV\"\n")

	print "SUCCESS: " + address
	time.sleep(0.05)
	counter = 0

g.close()
h.close()


