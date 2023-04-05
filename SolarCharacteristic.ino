// #define AIRSPEED_SENSOR
// #define LCD
#define SOLAR
// #define LOADCELL
#define RTC
// #define BAT
#define CURRENT_SENSOR
// #define SWITCH
#define PWM
#define DATALOGGER

// #define DEBUG_MODE

/************ AIRSPEED *************/
#ifdef AIRSPEED_SENSOR
  #include "ms4525do.h"
  bfs::Ms4525do ms4525do;
  float defaultPressureValue = 0.0;
  #define Nb_SAMPLE 10
  void calibAirspeedSensor() {
    for (int i=0; i<Nb_SAMPLE; i++) {
      while (!ms4525do.Read()) {}
      defaultPressureValue += ms4525do.pres_pa();
      delay(100);
    }
    defaultPressureValue = defaultPressureValue/ (float)Nb_SAMPLE;
    #ifdef DEBUG_MODE
      Serial.println("Default difference pressure: " + String(defaultPressureValue));
    #endif
  }

  float getAirspeed(float pressureDiff) {
    float deltaP = pressureDiff - defaultPressureValue;
    return sqrt(2*deltaP/1.225);
  }
#endif

/************ BATTERY **************/
#ifdef BAT
  #define BAT1
  // #define BAT2
#endif

#ifdef BAT1
  // SimpleKalmanFilter bat1VoltageFilter(1, 1, 0.3);

  #define PIN_BAT1 A6
  const float bat_1_voltage_divider_coefficient = 6.0;
  
  float getVoltageBat1() {
    int rawReading = analogRead(PIN_BAT1);
    float voltage =  5.0 * rawReading / 1023 * bat_1_voltage_divider_coefficient;
    return voltage;
  }

  // float getBat1FilteredVoltage() {
  //   return bat1VoltageFilter.updateEstimate(getVoltageBat1());
  // }
#endif

#ifdef BAT2
  // SimpleKalmanFilter bat2VoltageFilter(1, 1, 0.3);

  #define PIN_BAT2 A3
  const float bat_2_voltage_divider_coefficient = 6.0;

  float getVoltageBat2() {
    int rawReading = analogRead(PIN_BAT2);
    float voltage =  5.0 * rawReading / 1023 * bat_2_voltage_divider_coefficient;
    return voltage;
  }

  // float getBat2FilteredVoltage() {
  //   return bat2VoltageFilter.updateEstimate(getVoltageBat2());
  // }
#endif

/************ SOLAR **************/
#ifdef SOLAR
  #define PIN_SOLAR A2
  const float solar_voltage_divider_coefficient = 6.0;

  float getVoltageSolar() {
    int rawReading = analogRead(PIN_SOLAR);
    float voltage =  5.0 * rawReading / 1023 * solar_voltage_divider_coefficient;
    return voltage;
  }
#endif

/************ LOADCELL **************/
#ifdef LOADCELL
  #include "HX711.h"
  #define PIN_LOADCELL_DT A0
  #define PIN_LOADCELL_SCK A1

  HX711 loadcell;
  float calibrationFactor = 480.73;
  // const float calibObjectWeight = 390.0; //g

  bool calibLoadcell() {
    if (loadcell.is_ready()) {
      // loadcell.set_scale(); 
      // delay(500);
      // loadcell.tare();
      // loadcell.set_scale(calibrationFactor);
      return true;
    } 
    else {
      return false;
    }

  }

  float getWeight() {
    if (loadcell.wait_ready_timeout(200)) {
      return loadcell.get_units(5);
    }
    return 0.0;
  }
#endif

/************ RTC **************/
#ifdef RTC
  #define DS1307
  // #define DS1302
#endif

#ifdef DS1307
  #include <Wire.h> // must be included here so that Arduino library object file references work
  #include <RtcDS1307.h>
  RtcDS1307<TwoWire> Rtc(Wire);

  bool wasError(const char* errorTopic = "")
  {
    uint8_t error = Rtc.LastError();
    if (error != 0)
    {
        // we have a communications error
        // see https://www.arduino.cc/reference/en/language/functions/communication/wire/endtransmission/
        // for what the number means
        #ifdef DEBUG_MODE
          Serial.print("[");
          Serial.print(errorTopic);
          Serial.print("] WIRE communications error (");
          Serial.print(error);
          Serial.print(") : ");
        #endif
        

        switch (error)
        {
        case Rtc_Wire_Error_None:
            #ifdef DEBUG_MODE
            Serial.println("(none?!)");
            #endif
            break;
        case Rtc_Wire_Error_TxBufferOverflow:
            #ifdef DEBUG_MODE
            Serial.println("transmit buffer overflow");
            #endif
            break;
        case Rtc_Wire_Error_NoAddressableDevice:
            #ifdef DEBUG_MODE
            Serial.println("no device responded");
            #endif
            break;
        case Rtc_Wire_Error_UnsupportedRequest:
            #ifdef DEBUG_MODE
            Serial.println("device doesn't support request");
            #endif
            break;
        case Rtc_Wire_Error_Unspecific:
            #ifdef DEBUG_MODE
            Serial.println("unspecified error");
            #endif
            break;
        case Rtc_Wire_Error_CommunicationTimeout:
            #ifdef DEBUG_MODE
            Serial.println("communications timed out");
            #endif  
            break;
        }
        return true;
    }
    return false;
  }


  void syncTime() {
    RtcDateTime compiled = RtcDateTime(__DATE__, __TIME__);
    if (!Rtc.IsDateTimeValid()) 
    {
      if (!wasError("setup IsDateTimeValid"))
      {
        #ifdef DEBUG_MODE
        Serial.println("RTC lost confidence in the DateTime!");
        #endif
        Rtc.SetDateTime(compiled);
      }
    }

    if (!Rtc.GetIsRunning())
    {
      if (!wasError("setup GetIsRunning"))
      {
        #ifdef DEBUG_MODE
        Serial.println("RTC was not actively running, starting now");
        #endif
        Rtc.SetIsRunning(true);
      }
    }

    RtcDateTime now = Rtc.GetDateTime();
    if (!wasError("setup GetDateTime"))
    {
      if (now < compiled)
      {
        #ifdef DEBUG_MODE
        Serial.println("RTC is older than compile time, updating DateTime");
        #endif
        Rtc.SetDateTime(compiled);
      }
      else if (now > compiled)
      {
        #ifdef DEBUG_MODE
        Serial.println("RTC is newer than compile time, this is expected");
        #endif
      }
      else if (now == compiled)
      {
        #ifdef DEBUG_MODE
        Serial.println("RTC is the same as compile time, while not expected all is still fine");
        #endif
      }
    }

    // never assume the Rtc was last configured by you, so
    // just clear them to your needed state
    Rtc.SetSquareWavePin(DS1307SquareWaveOut_Low); 
    wasError("setup SetSquareWavePin");
  }
#endif

/************ CURRENT SENSOR **************/

#ifdef CURRENT_SENSOR
  // #define ACS_712_30A
  // #define ACS_712_20A
  #define ACS_712_5A
#endif

#define INTERNAL_RESISTOR 0.05767 // Ohm

double computeAdjustedVoltage(double voltage, double filteredCurrent) {
  return voltage + filteredCurrent * INTERNAL_RESISTOR;
}

#ifdef CURRENT_SENSOR
  #include "ACS712.h"
  #include <SimpleKalmanFilter.h>
  #define PIN_ACS712 A7
  const float VCC = 5.0;

  #ifdef ACS_712_30A
    float sensitivity = 66; // 100mV/A --> 20A, 185mV/A ---> 5A, 66mv/A -->30A
  #elif ACS_712_20A
    float sensitivity = 100;
  #else
    float sensitivity = 185;
  #endif
  
  
  SimpleKalmanFilter currentFilter(1, 1, 0.3);
  ACS712  ACS(PIN_ACS712, VCC, 1023, sensitivity);

  float getCurrent() {
  return ACS.mA_DC()/1000.0;
  }

  float getFilteredCurrent(float current) {
    return currentFilter.updateEstimate(current);
  }
#endif

/************ SWITCH **************/
#ifdef SWITCH
  #define PIN_SW1 7
  #define PIN_SW2 8
#endif

/************ PWM *************/
#ifdef PWM
  #define PIN_PWM 9
#endif

/************ DATALOGGER *************/
#ifdef DATALOGGER
  #define DATALOGGER_CS 10
  
  #include <SPI.h>
  #include <SD.h>

  #define MESSAGE_SIZE sizeof(Message)
  const int chipSelect = 10; // CS
  File dataFile;
  String fileName = "data.txt";
  bool bIsRecording = false;

  void logData(){    
    if (!bIsRecording) {
      dataFile = SD.open(fileName, FILE_WRITE);
      String data = "hour:minute:second current voltage";
      dataFile.println(data);
      dataFile.close();
      bIsRecording = true;
    }
    else {
      dataFile = SD.open(fileName, FILE_WRITE);
      
      #ifdef RTC
        RtcDateTime currentTime = Rtc.GetDateTime();
        uint8_t hour = (uint8_t)(currentTime.Hour());
        uint8_t minute = (uint8_t)(currentTime.Minute());
        uint8_t second = (uint8_t)(currentTime.Second());
      #endif
      
      #ifdef CURRENT_SENSOR
        double rawCurrent = getCurrent();
        double filteredCurrent = getFilteredCurrent(rawCurrent);
      #endif
      
      #ifdef BAT1
        double voltage = getVoltageBat1();
      #endif

      #ifdef BAT2
        double voltage = getVoltageBat2();
      #endif

      #ifdef SOLAR
        double voltage = getVoltageSolar();
      #endif
      
      String data = "";
      #ifdef RTC 
        data += String(hour) + ":" + String(minute) + ":" + String(second);
      #endif
      #ifdef CURRENT_SENSOR
        data +=  " " + String(filteredCurrent);
        #ifdef DEBUG_MODE
        Serial.println("Current: " + String(filteredCurrent) + " A");
        Serial.println("Power: " + String(filteredCurrent*voltage) + " W");
        #endif
      #endif
      #ifdef SOLAR            
        data +=  " " + String(voltage);
        #ifdef DEBUG_MODE
        Serial.println("Voltage: " + String(voltage) + " V");
        #endif
      #endif
          
      dataFile.println(data);
      dataFile.close();      
      }
    }
  #endif

/**********************************/
void setup() {
  bool isAllInitiated = true;
  #ifdef DEBUG_MODE
  Serial.begin(57600);
  #endif

  #ifdef PWM
    pinMode(PIN_PWM, OUTPUT);
  #endif

  #ifdef CURRENT_SENSOR
    pinMode(PIN_ACS712, INPUT);
    ACS.autoMidPoint();
  #endif

  #ifdef SOLAR
    pinMode(PIN_SOLAR, INPUT);
  #endif

  #ifdef RTC
    Rtc.Begin();
    // Wire.setWireTimeout(3000 /* us */, true /* reset_on_timeout */);
    // syncTime();
  #endif
  
  #ifdef DATALOGGER
    isAllInitiated =  isAllInitiated && SD.begin(DATALOGGER_CS);
  #endif

  #ifdef DEBUG_MODE
  if (isAllInitiated) {
    Serial.println("All devices are well initiated");
  }
  else {
    Serial.println("Some devices are not well initiated");
  }
  #endif

    #ifdef DATALOGGER
    for (int i=254; i>=0; i--) {
      analogWrite(PIN_PWM, i);
      logData();
      delay(150);
    }
  #endif
}

void loop() {
}
