@Grapes([
    @Grab("mysql:mysql-connector-java:6.0.5"),
    @GrabConfig(systemClassLoader = true)
])

import groovy.sql.Sql
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Paths

// Database
def dbServer = "localhost"
def dbName = "groovy_wrk"
def dbPort = "3306"
def timezone = "serverTimezone=JST"
def url = "jdbc:mysql://${dbServer}:${dbPort}/${dbName}?${timezone}"
def user = "root"
def password = ""
def driver = "com.mysql.cj.jdbc.Driver"
def database = Sql.newInstance(url, user, password, driver)

def query = """
    LOAD DATA LOCAL INFILE 'user.csv' 
    INTO TABLE 
	    user 
    FIELDS TERMINATED BY ',' 
    IGNORE 1 LINES 
"""

// File
def spa = FileSystems.getDefault().getSeparator()
def input_csv = new File("user.csv")
def imported_foder = new Date().format('yyyyMMdd')
def sourcePath = Paths.get("." + spa + input_csv)
def targetPath = Paths.get("." + spa + imported_foder + spa + input_csv)
def imported = new File("${targetPath}")

if(!input_csv.exists()){
	println "user.csv not found"
    return
}

if(imported.exists()){
    println "today's user.csv is imported"
    return
}   

database.execute(query)

new File("${imported_foder}").mkdir()
Files.move(sourcePath, targetPath)
