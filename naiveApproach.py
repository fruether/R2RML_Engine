import os


projects = ["ROMS", "projectforge-webapp", "Oscar", "oltpbench", "Mateo", "iTests-Framework", "frontlinesms-core", "IBPMiddleware", "eeg-database",  "druid"]

def get_dao_data(project):
	countDao = 0
	saveDaos = []
	path = "/Users/freddy/Desktop/repos_temp/" +project
	for root, dirs, files in os.walk(path):
		for file in files:
			if file.endswith("Dao.java") or file.endswith("DAO.java"):
				filePath = os.path.join(root, file)
				with open(filePath, 'r') as javaFile:
					content = javaFile.read()
					if "interface" not in content:
						#saveDaos.append(os.path.join(root, file))
						countDao += 1
	print countDao
	print "Daos: "
	for dao in saveDaos:
		print dao 

def get_xml_mapping(project):
	saveXMLMapping = []
	countXMLMapping = 0
	path = "/Users/freddy/Desktop/repos_temp/" + project

	for root, dirs, files in os.walk(path):
		for file in files:
			if file.endswith(".hbm.xml"):
				saveXMLMapping.append(os.path.join(root, file))
				countXMLMapping += 1
	print countXMLMapping   

def get_annotation_mapping(project):
	saveAnnotationMapping = []
	countAnnotationMapping = 0
	path = "/Users/freddy/Desktop/repos_temp/" + project

	for root, dirs, files in os.walk(path):
		for file in files:
			if(file.endswith(".java")):
				filePath = os.path.join(root, file)	
				with open(filePath, 'r') as javaFile:
					content = javaFile.read()
					if "@Entity" in content:
						saveAnnotationMapping.append(filePath)
						countAnnotationMapping += 1
	print countAnnotationMapping   		

#get_dao_data("frontlinesms-core")
for project in projects:
	print project
	get_dao_data(project)