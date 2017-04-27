import json
import requests
import sys
from datetime import datetime
import time

f = open("/home/ubuntu/application-project/data/raw/other-Firstclass_B01536.csv", "r")
data = f.readlines()

result = data[0]

g = open("/home/ubuntu/application-project/data/result/Firstclass.csv", "a+")
h = open("/home/ubuntu/application-project/data/result/Firstclass-Errors", "a+")

counter = 0

for i in range(140000, len(data)):
	element = [x.strip() for x in data[i].split(",")]

	address = " ".join( element[ 2:len(element) ] )

	address = address.replace("NYC", "New York City NY")
	address = address.replace("BX", "Bronx").replace("Bronx", "Bronx NY")
	address = address.replace("\"", "").strip()

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

	datetime_object = datetime.strptime(element[0] + " " + element[1], '%m/%d/%Y %I:%M:%S %p')

	g.write( "\"" + datetime_object.strftime('%m/%d/%Y %H:%M:%S') + "\"," + str(result["results"][0]["geometry"]["location"]["lat"]) + "," + str(result["results"][0]["geometry"]["location"]["lng"]) + ",\"B01536\",\"Firstclass\",\"FHV\"\n")

	print "SUCCESS: " + address
	time.sleep(0.1)
	counter = 0

g.close()
h.close()


