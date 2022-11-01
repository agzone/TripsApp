# TripsApp

This is a demo appliction for transforming a list of taps, 
representing touch ONs and touch OFFs using a payment card to complete a trip. 
Taps are read from an input file and converted into trips written to an output file.

## Run the application
Follow the steps below to build and run the application using **gradle**.
### Build
Execute the command below to build the project.
```
./gradlew clean build
```
### Test
Execute the command below to run all tests.
```
./gradlew clean test
```
### Run
Execute the command below to run the application with gradle.
Two arguments must be provided in the following order.
- Path to source file containing taps to read.
- Path to output file to write.

Replace the example paths below with real paths before executing the command.  
```
./gradlew run --args="$HOME/inputdir/taps.csv $HOME/outputdir/trips.csv"
```
Alternatively, execute the "fat" jar-file as below.
```
java -jar build/libs/TripsApp-1.0-all.jar $HOME/inputdir/taps.csv $HOME/outputdir/trips.csv
```
## Assumptions / Limitations
The follwing assumptions and limitations apply to this implementation.
- All taps for a certain trip are present in the input file. For example, the first tap of trip using a specific PAN is always a tap ON.
- There cannot be 2 tap OFFs in a row for a PAN, based on taps ordered by their timestamp.
- If a touch ON and touch OFF occur at the same stop, the trip is considered cancelled. No time constraints are considered. 
- All records in the input file contain only valid data and no "corrupt" records with invalid or only partial information are present.
- The input files to process aren't larger than what the machine executing the program can handle in-memory.
- Error handling is kept to a minimum. A real production implementation would need more robust error handling.

## Examples
An example of each possible trip status is shown below.
### Completed trip
#### Taps (input)
```
ID, DateTimeUTC, TapType, StopId, CompanyId, BusID, PAN
1, 02-01-2022 07:00:00, ON, Stop1, Company1, Bus1, 4444333322221111
1, 02-01-2022 08:00:00, OFF, Stop2, Company1, Bus1, 4444333322221111
```
#### Trips (output)
```
Started, Finished, DurationSecs, FromStopId, ToStopId, ChargeAmount, CompanyId, BusID, PAN, Status
02-01-2022 07:00:00, 02-01-2022 08:00:00, 3600, Stop1, Stop2, $3.25, Company1, Bus1, 4444333322221111, COMPLETED
```
### Incomplete trip
#### Taps (input)
```
ID, DateTimeUTC, TapType, StopId, CompanyId, BusID, PAN
1, 02-01-2022 07:00:00, ON, Stop2, Company1, Bus1, 4444333322221111
```
#### Trips (output)
```
Started, Finished, DurationSecs, FromStopId, ToStopId, ChargeAmount, CompanyId, BusID, PAN, Status
02-01-2022 07:00:00, , , Stop2, , $5.50, Company1, Bus1, 4444333322221111, INCOMPLETE
```
### Cancelled trip
#### Taps (input)
```
ID, DateTimeUTC, TapType, StopId, CompanyId, BusID, PAN
1, 02-01-2022 07:00:00, ON, Stop1, Company1, Bus1, 4444333322221111
1, 02-01-2022 08:00:00, OFF, Stop1, Company1, Bus1, 4444333322221111
```
#### Trips (output)
```
Started, Finished, DurationSecs, FromStopId, ToStopId, ChargeAmount, CompanyId, BusID, PAN, Status
02-01-2022 07:00:00, 02-01-2022 08:00:00, 3600, Stop1, Stop1, $0.00, Company1, Bus1, 4444333322221111, CANCELLED
```