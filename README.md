# Connected Car Project - Data Processing (v 1.0)
After exploring the different data formats of the connected car (tdms, gps, and txt), which were shared on a fileshare in Azure, the first objective was to make this data in a valid format in order to send it to a time-series database InfluxDB.  
The idea was to automate the data preprocessing process using Azure function Blob Trigger, which triggers the execution of the script when a new file is sent to the input blob container(or a file is modified), and the python script can process the received data and sends it to InfluxDB.  
To visualize this data we chose Grafana which can easily connect to influxDB, both are installed on an Azure virtual machine, and the solution is hosted on the Nginx web server.   
Below is the architecture of the project.
<br/><br/>
![Project diagram](https://sensordatamining.blob.core.windows.net/vehicule-data-output-2/Shema2.PNG
 "Project diagram")
 <br/><br/>
 # Project Demonstration
 
The first step is the acquisition of data from the connected car, there are 4 types of file extensions, tdms files which represent the acceleration data, gps files for tracking gps data, and txt files that contain bus can data with differents variables (ANGLE_VOLANT, REGIME_MOTEUR ...), the duration of every file is 10 minutes.    
After the acquisition, data is sent to an Azure file share with the name "sdmsharefile", and located in "cssdm" storage account.  
   <br/><br/> 
 ![fileShare](https://sensordatamining.blob.core.windows.net/vehicule-data-output-2/Capture17.PNG
 "fileShare")
 <br/><br/>
 ## Blob Storage
 
After that, data is sent to blob storage, we need to do that in order to automatically trigger the Azure function when a new file is added. For sending data to the blob trigger we use the az copy function. The blob container name is "vehicule-data-input" and it is located in "sensordatamining" storage account.
  <br/><br/> 
  ![blob](https://sensordatamining.blob.core.windows.net/vehicule-data-output-2/Capture14.PNG
 "blob") 
 <br/><br/>
 The folowing is the az copy command i use to send data to blob from fileshare.
 <br/><br/> 
  ![azcopy](https://sensordatamining.blob.core.windows.net/vehicule-data-output-2/Capture16.PNG
 "azcopy")  
 <br/><br/>
 ## Azure Blob Trigger
 Once a new file is added, the Azure function BlobTrigger is triggered, this function is deployed in the Cloud with the name "vehicule-data-cleaning", and the main functionalities of it is :
  <br/><br/> 
  ![blobtrigger](https://sensordatamining.blob.core.windows.net/vehicule-data-output-2/Capturere13.PNG
 "blobtrigger")  
    <br/><br/>
* Acquisition and preprocessing of connected car data.
* Send processed data to time series database influxDB.

The function main files are the following (All files are generated by default when creating the function):
__init__.py : is the file that contains the body of the function, it includes all the functions that prepare and send data to influxdb database, and the code is developped using python.
function.json : this file defines the function's trigger, bindings, and other configuration settings. Every function has one and only one blob trigger.
requirements.txt : this file contains a list of items to be installed using pip install
host.json : this file contains some sittings of the function, i used it to specify the function execution timeout  which is set to 00:40:00 in our case.
local.settings.json : this file is for function local settings in case you want to download the function and test it in your local machine.


### Prerequisites
In order to run the function locally for developpement or test purposes you need:

* install Azure Functions Core Tools. 
* Python installed on a windows machine.
* Visual Studio Code text editor.
* Add the folowing extensions to VS Code : Azure account, Azure functions, Azure ressources  


### Installing

Clone the directory on your local machine  
```
git clone https://51.144.178.151/lailasabar/sensorsdatamining
 ```
After cloning the project,install the "requirements.txt" on your virtualenv

```
pip install -r requirements.txt
```

### Running the programme
* Open the cmd on the containing folder and activate the virtual envirement, then use the command :
```
func start
```
### Built With

* [Azure function - Blob Trigger](https://docs.microsoft.com/fr-fr/azure/azure-functions/functions-bindings-storage-blob-trigger?tabs=csharp) - The Blob storage trigger starts a function when a new or updated blob is detected. 
* [Python](https://www.python.org/) - A programming language that allows facilitating data processing
* [Azure Blob Storage](https://azure.microsoft.com/fr-fr/services/storage/blobs/) -  Blob Storage is optimized for storing massive amounts of unstructured data.

## Grafana dashboard
To acces grafana dashboard you shoud type the following URL in the browser then insert credentials (user: Admin / pass: Hutchinson2021)

After sending data to influxdb database we need to connect grafana to influxdb database, in order to visualize data, for that we need to create a new data source in grafana, or directly use it if it is already created.
<br/><br/> 
  ![blobtrigger](https://sensordatamining.blob.core.windows.net/vehicule-data-output-2/Capture4.PNG
 "blobtrigger")
  <br/><br/> 
  ![blobtrigger](https://sensordatamining.blob.core.windows.net/vehicule-data-output-2/Capture9.PNG
 "blobtrigger")  
    <br/><br/>
  ![blobtrigger](https://sensordatamining.blob.core.windows.net/vehicule-data-output-2/Capture10.PNG
 "blobtrigger")  
    <br/><br/>
These are the database connection credentials needed to connect influxdb database to grafana:
* user: telegraf
* password: Hutchinson2021
* database: ConnectedCar_data 
* port: 8086
* host: localhost
  <br/><br/> 
  ![blobtrigger](https://sensordatamining.blob.core.windows.net/vehicule-data-output-2/capture6.PNG
 "blobtrigger")  
    <br/><br/>
  ![blobtrigger](https://sensordatamining.blob.core.windows.net/vehicule-data-output-2/Capture7.PNG
 "blobtrigger")  
    <br/><br/>
After preparing and organizing data coming from connected vehicule,  sending data to influxdb and connecting it with grafana, at this part we have all ingradients in order vizuaize our data ina a beautiful responsive dashboard that displays various signals patterns and GPS routes for a selected period of time.     
the dashboard name is test_trackmap.
<br/><br/>
  ![blobtrigger](https://sensordatamining.blob.core.windows.net/vehicule-data-output-2/Capture1.PNG
 "blobtrigger")  
    <br/><br/>
the dashborad contains a map with name "GPS TRACKING " that track gps data of the car, this map has the following featureas :

* Places a dot on the map at the current time as you mouse over other panels.
* Zoom to a range of points by drawing a box by shift-clicking and dragging.
* Multiple map backgrounds: OpenStreetMap, OpenTopoMap, and Satellite imagery.
* Track and dot colors can be customized in the options tab.
<br/><br/>
  ![blobtrigger](https://sensordatamining.blob.core.windows.net/vehicule-data-output-2/Capture2.PNG
 "blobtrigger")  
    <br/><br/>
In Addition to map we have some time series visualizations, one for acceleration data, and the rest for bus can data.  
For acceleration data we have to dropdown variables (acc_measures, acc_axes) and an input variable (acc_gt), the user should select the type of accelerator (monoaxe or triaxe) and also the aggregation function (min,max, mean, rms, std ) from the variable acc_measures, then select the wanted axe of accelerator from the variable acc_axes, and with the input variable acc_gt the user can insert a float number and the visualization will show juste the acceleration values greater that the inserted number.   
Bus can data is seperated in three differents visualization panels, evry panel shows the variable with the same scale, you can select to show one  graphe by clicking on the variable on the legend, or you can select more by clicking on control then selecting all the wanted variables.
<br/><br/>
  ![blobtrigger](https://sensordatamining.blob.core.windows.net/vehicule-data-output-2/Capture11.PNG
 "blobtrigger") 
<br/><br/>
  ![blobtrigger](https://sensordatamining.blob.core.windows.net/vehicule-data-output-2/Capture12.PNG
 "blobtrigger")  
<br/>
  ![blobtrigger](https://sensordatamining.blob.core.windows.net/vehicule-data-output-2/Capture3.PNG
 "blobtrigger")  
    <br/><br/>
