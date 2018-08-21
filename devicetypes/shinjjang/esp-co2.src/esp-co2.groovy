/**
 *  ESP Easy CO2 DTH (v.0.0.1)
 *
 * MIT License
 *
 * Copyright (c) 2018 
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */
 
import groovy.json.JsonSlurper
import groovy.transform.Field


metadata {
	definition (name: "ESP CO2", namespace: "ShinJjang", author: "ShinJjangwithFison67") {
        capability "Sensor"
		capability "Temperature Measurement"
        capability "Carbon Dioxide Measurement"
        capability "Illuminance Measurement"
        capability "Refresh"
        
		attribute "infraredIndex", "number"
		attribute "broadband", "number"
		attribute "maxLux", "number"
		attribute "minLux", "number"
		attribute "maxTemp", "number"
		attribute "minTemp", "number"
		attribute "maxCo", "number"
		attribute "minCo", "number"
        attribute "lastCheckinDate", "date"
        
        command "setData"
        command "refresh"
        command "timerLoop"
        command "averageCo"
		command	"checkNewDay"

	}


	simulator {
	}
    preferences {
		input "url", "text", title: "ESP IP주소", description: "로컬IP 주소를 입력", required: true
        input "refreshRateMin", "enum", title: "CO2 업데이트 주기", defaultValue: 60, options:[5: "5 sec", 10: "10 sec", 20: "20 sec", 30 : "30 sec", 60: "1 min", 120 :"2 min", 180 :"3 min", 300 :"5 min", 600: "10 min", 900: "15 min", 1200: "20 min", 1800: "30 min"], displayDuringSetup: true
    }

	tiles(scale: 2) {
    		multiAttributeTile(name:"main", type:"generic", width:6, height:4) {
			tileAttribute("device.carbonDioxide", key: "PRIMARY_CONTROL") {
            	attributeState "carbonDioxide",label:'${currentValue}', backgroundColors:[
 				[value: 400, color: "#a3da91"],
 				[value: 600, color: "#d8e288"],
                [value: 999, color: "#f2d33c"],
                [value: 1500, color: "#dd6637"],
                [value: 2500, color: "#77110b"]
				]
            }
            tileAttribute ("device.lastCheckin", key: "SECONDARY_CONTROL") {
				attributeState "lastCheckin", label:'Updated: ${currentValue}'
			}
		} 
        valueTile("list", "device.carbonDioxide") {
            state "carbonDioxide",label:'${currentValue}ppm', icon:"https://postfiles.pstatic.net/MjAxODA0MThfOCAg/MDAxNTIzOTk1MzE3MjY2.cDoiO_gS9Kruy61OR8Ek_TKcph10ygy2_-rwzJAs-aUg.1zZ_dy3o2kYf9aMXFNoTytnX5opVUl5Ut4yOL7NmPQQg.PNG.shin4299/planet_%281%29.png?type=w580", backgroundColors:[
 				[value: 400, color: "#a3da91"],
 				[value: 600, color: "#d8e288"],
                [value: 999, color: "#f2d33c"],
                [value: 1500, color: "#dd6637"],
                [value: 2500, color: "#77110b"]
				]
            }
        valueTile("temperature", "device.temperature", width:2, height:2) {
            state "temperature", label:'${currentValue}°', defaultState: true,
            backgroundColors:[
                        [value: 0, color: "#153591"],
                        [value: 5, color: "#1e9cbb"],
                        [value: 10, color: "#90d2a7"],
                        [value: 15, color: "#44b621"],
                        [value: 20, color: "#f1d801"],
                        [value: 25, color: "#d04e00"],
                        [value: 30, color: "#bc2323"],
                        [value: 44, color: "#1e9cbb"],
                        [value: 59, color: "#90d2a7"],
                        [value: 74, color: "#44b621"],
                        [value: 84, color: "#f1d801"],
                        [value: 95, color: "#d04e00"],
                        [value: 96, color: "#bc2323"]
            ]
        }        
        valueTile("temp_label", "", decoration: "flat", width:2, height:1) {
            state "default", label:'현재온도'
        }
        valueTile("refresh_label", "", decoration: "flat", width:2, height:1) {
            state "default", label:'새로고침'
        }
        valueTile("tempXN_label", "", decoration: "flat", width:4, height:1) {
            state "default", label:'오늘 온도변화'
        }
        valueTile("maxTemp", "device.maxTemp", decoration: "flat", width:2, height:1) {
            state "default", label:'최고 ${currentValue}°'
        }
        valueTile("maxTempTime", "device.maxTempTime", decoration: "flat", width:2, height:1) {
            state "default", label:'${currentValue}'
        }
        valueTile("minTemp", "device.minTemp", decoration: "flat", width:2, height:1) {
            state "default", label:'최저 ${currentValue}°'
        }
        valueTile("minTempTime", "device.minTempTime", decoration: "flat", width:2, height:1) {
            state "default", label:'${currentValue}'
        }
        valueTile("COXN_label", "", decoration: "flat", width:4, height:1) {
            state "default", label:'오늘 CO2변화'
        }
        valueTile("maxCo", "device.maxCo", decoration: "flat", width:2, height:1) {
            state "default", label:'최고 ${currentValue}ppm'
        }
        valueTile("maxCoTime", "device.maxCoTime", decoration: "flat", width:2, height:1) {
            state "default", label:'${currentValue}'
        }
        valueTile("minCo", "device.minCo", decoration: "flat", width:2, height:1) {
            state "default", label:'최저 ${currentValue}ppm'
        }
        valueTile("minCoTime", "device.minCoTime", decoration: "flat", width:2, height:1) {
            state "default", label:'${currentValue}'
        }
        standardTile("refresh", "device.refresh", width:2, height:2) {
            state "default", label:"", action:"refresh", icon:"st.secondary.refresh", backgroundColor:"#A7ADBA"
        }
		valueTile("illuminance", "device.illuminance", inactiveLabel: false, width: 2, height: 2) {
           state "luminosity", label:'${currentValue} lux', unit:"lux", 
                backgroundColors:[
                	[value: 0, color: "#000000"],
                    [value: 1, color: "#060053"],
                    [value: 3, color: "#3E3900"],
                    [value: 12, color: "#8E8400"],
					[value: 24, color: "#C5C08B"],
					[value: 36, color: "#DAD7B6"],
					[value: 128, color: "#F3F2E9"],
                    [value: 1000, color: "#FFFFFF"]
				]
		}
        
		valueTile(
        	"infraredIndex","device.infraredIndex", inactiveLabel: false, width: 2, height: 2) {
				state "infraredIndex",label:'${currentValue} IR INDEX',unit:""
		}
		valueTile(
        	"broadband","device.broadband", inactiveLabel: false, width: 2, height: 2) {
				state "broadband",label:'${currentValue} Broadband',unit:"",
                backgroundColors:[
                	[value: 0, color: "#000000"],
                    [value: 1, color: "#060053"],
                    [value: 3, color: "#3E3900"],
                    [value: 12, color: "#8E8400"],
					[value: 24, color: "#C5C08B"],
					[value: 36, color: "#DAD7B6"],
					[value: 128, color: "#F3F2E9"],
                    [value: 1000, color: "#FFFFFF"]
				]
		}


        
       
       	main (["list"])
      	details(["main",
                 "illuminance", "infraredIndex", "broadband", 
                 "temp_label","tempXN_label", 
        		 "temperature", "maxTemp", "maxTempTime", "minTemp", "minTempTime",
                 "refresh_label", "COXN_label", 
                 "refresh", "maxCo", "maxCoTime",  "minCo", "minCoTime"])
	}
}


def parse(String description) {
	log.debug "Parsing '${description}'"
}

def updated() {
    log.debug "URL >> ${url}"
	state.address = url
    state.lastTime = new Date().getTime()
    state.timeSecond = refreshRateMin
    state.averageNumber = averageNumber as int
    
    averageReset()
    timerLoop()
}

def setData(data){
	state._data = data
    
    try{
        data.each{item->
            
            if(item.Type == "Gases - CO2 MH-Z19"){
            	state.carbonDioxide = item.TaskValues[0].Value
                state.temperature = item.TaskValues[1].Value
                log.debug "CO >> ${state.carbonDioxide}"
                log.debug "Temp >> ${state.temperature}"
                sendEvent(name: "carbonDioxide", value: state.carbonDioxide, unit: "ppm" )
                updateMinMaxCo(state.carbonDioxide)
                sendEvent(name: "temperature", value: state.temperature, unit: "C")
                updateMinMaxTemps(state.temperature)
            } 
            if(item.Type == "Light/Lux - TSL2561"){
            	state.lux = item.TaskValues[0].Value
                state.red = item.TaskValues[1].Value
                state.broad = item.TaskValues[2].Value
                log.debug "Lux >> ${state.lux}"
                log.debug "Infrared >> ${state.red}"
                log.debug "Broadband >> ${state.broad}"
                sendEvent(name: "illuminance", value: state.lux, unit: "ppm" )
                sendEvent(name: "infraredIndex", value: state.red )
                sendEvent(name: "broadband", value: state.broad )
//                updateMinMaxCo(state.carbonDioxide)
//                averageCo(state.carbonDioxide)
//                sendEvent(name: "temperature", value: state.temperature, unit: "C")
//                updateMinMaxTemps(state.temperature)
            }
        }
		def nowk = new Date().format("yyyy-MM-dd HH:mm:ss", location.timeZone)
        def now = new Date()
        state.lastTime = now.getTime()
        sendEvent(name: "lastCheckin", value: nowk)
        checkNewDay()
    }catch(e){
    	log.error "Error!!! ${e}"
    }
}

def checkNewDay() {
	def now = new Date().format("yyyy-MM-dd", location.timeZone)
	if(state.prvDate == null){
		state.prvDate = now
	}else{
		if(state.prvDate != now){
			state.prvDate = now
			    log.debug "checkNewDay _ ${state.prvDate}"
			resetMinMax()            
		}
	}
}

def resetMinMax() {	
	def currentTemp = device.currentValue('temperature')
	def currentCo = device.currentValue('carbonDioxide')
    currentTemp = currentTemp ? (int) currentTemp : currentTemp
	log.debug "${device.displayName}: Resetting daily min/max values to current temperature of ${currentTemp}° and humidity of ${currentCo}%"
    sendEvent(name: "maxTemp", value: currentTemp)
    sendEvent(name: "minTemp", value: currentTemp)
    sendEvent(name: "maxCo", value: currentCo)
    sendEvent(name: "minCo", value: currentCo)
    refreshMultiAttributes()
}

def updateMinMaxTemps(temp) {
	def ttime = new Date().format("a hh:mm", location.timeZone)
	if ((temp > device.currentValue('maxTemp')) || (device.currentValue('maxTemp') == null)){
		sendEvent(name: "maxTemp", value: temp)	
		sendEvent(name: "maxTempTime", value: ttime)	
	} else if ((temp < device.currentValue('minTemp')) || (device.currentValue('minTemp') == null)){
		sendEvent(name: "minTemp", value: temp)
		sendEvent(name: "minTempTime", value: ttime)
    }
}

def updateMinMaxCo(Co) {
	def ttime = new Date().format("a hh:mm", location.timeZone)
	if ((Co > device.currentValue('maxCo')) || (device.currentValue('maxCo') == null)){
		sendEvent(name: "maxCo", value: Co)
		sendEvent(name: "maxCoTime", value: ttime)	
	} else if ((Co < device.currentValue('minCo')) || (device.currentValue('minCo') == null)){
		sendEvent(name: "minCo", value: Co)
		sendEvent(name: "minCoTime", value: ttime)
    }
}

def timerLoop() {
	getStatusOfESPEasy()    
	startTimer(state.timeSecond.toInteger(), timerLoop)
}

def startTimer(seconds, function) {
    def now = new Date()
	def runTime = new Date(now.getTime() + (seconds * 1000))
	runOnce(runTime, function) // runIn isn't reliable, use runOnce instead
}

def callback(physicalgraph.device.HubResponse hubResponse){
	def msg, json, status
    try {
        msg = parseLanMessage(hubResponse.description)
        def jsonObj = msg.json
        setData(jsonObj.Sensors)
        
    } catch (e) {
        log.error "Exception caught while parsing data: " + e 
    }
}

def refresh() {
	averageReset()
	timerLoop()
}

include 'asynchttp_v1'

def getStatusOfESPEasy() {
    try{
    	def timeGap = new Date().getTime() - Long.valueOf(state.lastTime)
        if(timeGap > 3000 * state.timeSecond.toInteger()){
            log.warn "ESP Easy device is not connected..."
        }
		log.debug "Try to get data from ${state.address}"
        def options = [
            "method": "GET",
            "path": "/json",
            "headers": [
                "HOST": state.address + ":80",
                "Content-Type": "application/json"
            ]
        ]
        def myhubAction = new physicalgraph.device.HubAction(options, null, [callback: callback])
        sendHubCommand(myhubAction)
    }catch(e){
    	log.error "Error!!! ${e}"
    }
}