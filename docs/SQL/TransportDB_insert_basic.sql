-- Insert data into the ShippingAreas table
INSERT INTO ShippingAreas (areaNumber, areaName) VALUES
                        (0, 'Central District'),
                        (1, 'South District'),
                        (2, 'East District');

-- Insert data into the Sites table
INSERT INTO Sites (areaNum, addressStr, contName, contNumber) VALUES
                        (0, 'Tel Aviv', 'Yossi Oren', 0542315421),
                        (1, 'Ben Gurion Uni', 'Meni Adler', 0526451234),
                        (2, 'Afula', 'Dani Hendler', 0535471594);

-- Insert data into the Trucks table
INSERT INTO Trucks (truckNum, model, netWeight, maxCarryWeight, validLicense, inTransportID) VALUES
                        (401, 'Truck Model A', 1000, 20, 'A', -1),
                        (402, 'Truck Model B', 15000, 120, 'E', 1),
                        (403, 'Truck Model C', 12000, 100, 'C', -1);

-- Insert data into the Transports table
INSERT INTO Transports (tranDocId, status, departure_dt, transportTruckNumber, transportDriverId, truck_Depart_Weight, srcSiteArea, srcSiteString, isQueued) VALUES
    (1, 'InTransit', '2025-06-10 09:00:00', 402, 555555555, 80, 2, 'Afula', false);

-- Insert data into the Counters table
INSERT INTO Counters (CounterName, CounterValue) VALUES
                        (  'transportIDCounter', 1);

-- Insert data into DriverIdToInTransportID table
INSERT INTO DriverIdToInTransportID (transportDriverId, transportId) VALUES
                         (555555555, 1);

-- Insert data into TransportsProblems table
INSERT INTO TransportsProblems (problemOfTranDocId, problem) VALUES
                          (1, 'HeavyTraffic');

-- Insert data into ItemsDocs table
INSERT INTO ItemsDocs (itemsDocNum, ItemsDocInTransportID, srcSiteArea, srcSiteString, destSiteArea, destSiteString, estimatedArrivalTime) VALUES
                           (1, 1, 2, 'Afula', 0, 'Tel Aviv', '2025-06-10 10:00:00'),
                           (2, 1, 2, 'Afula', 1, 'Ben Gurion Uni', '2025-06-10 11:00:00');

-- Insert data into ItemsQ table
INSERT INTO ItemsQ (itemInItemsDocId, name, weight, condition, amount) VALUES
                           (1, 'Neviot Water', 1.5, true, 20),
                           (1, 'Nivea Body Cream', 1, true, 40),
                           (2, 'Berman Whole Wheat Bread', 0.5, true, 20);















