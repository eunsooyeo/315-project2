
import csv
import random


## drinkAndPrice csv filler
def drinkAndPriceList():
    filename = "drinkandprice.csv"

    #initializing
    global drinkAndPrice

    #reading file
    with open(filename) as csvfile:
        csvreader = csv.reader(csvfile)

        #extracting data
        drinkAndPrice = [row for row in csvreader]

#call function
drinkAndPriceList()

# Incremented in the orderDay function
date = "January 1 2023"

filename = "OrderHistory.csv"

# 'listOfOrders' keeps track of the whole order, it is a list of list
# 'tmpListOfOrders' keeps track of the order for a day and sorts it by time (hours, minutes, seconds)
listOfOrders = []
tmpListOfOrders = []

# auto incremented
orderID = 1

monthsWith31Days = {"January", "March", "May", "July", "August", "October", "December"}
months = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"]

# Busy days are set to be Monday and Friday currently, the two peak days 
minOrderForNormalDays = 200
maxOrderForNormalDays = 400
minOrderForBusyDays = 300
maxOrderForBusyDays = 500

# Increments date, switch to new month when necessary
# Uses the set 'monthsWith31Days' and list 'months' for conveniency
def incrementDate():
    global date
    arr = date.split(" ")
    if(arr[0] in monthsWith31Days):
        if(int(arr[1]) == 31):
            if(months.index(arr[0]) == 11):
                date = "January 1 2024"
            else:
                date = months[months.index(arr[0]) + 1] + " 1 " + arr[2]
        else:
            date = arr[0] + " " + str(int(arr[1]) + 1) + " " + arr[2]
    else:
        if(arr[0] == "February" and arr[1] == 28):
            date = "March 1 2023"
        elif(int(arr[1]) == 30):
            date = months[months.index(arr[0]) + 1] + " 1 " + arr[2]
        else:
            date = arr[0] + " " + str(int(arr[1]) + 1) + " " + arr[2]

    
# Function for one order
def order(openHour, closeHour):
    global orderID
    orderList = [orderID, date]

    hour = random.randint(openHour, closeHour)
    min = random.randint(0, 59)
    sec = random.randint(0, 59)
    orderList.append(str(hour)+":"+str(min)+":"+str(sec))

    ##need to generate number of drinks and drink number 1-19
    numberOfDrinks = random.randint(1, 3)
    drinkNumbers = []
    for i in range(numberOfDrinks):
        drinkNumbers.append(random.randint(0, 18))

    cost = 0.0
    namesOfDrinks = ""
    for i in drinkNumbers:
        cost += float(drinkAndPrice[i][1])
        namesOfDrinks += drinkAndPrice[i][0] + ", "
    ##remove extra comma and space
    namesOfDrinks = namesOfDrinks[0:-2]

    orderList.append(cost)
    orderList.append(namesOfDrinks)

    orderID += 1

    global tmpListOfOrders
    tmpListOfOrders.append(orderList)

# Function that sorts based on the 3rd column, used in orderDay
def sortFn(list):
    return list[2]

# Function for day of orders
def orderDay(openHour, closeHour, minOrder, maxOrder):
    numberOfOrders = random.randint(minOrder, maxOrder)
    for i in range(numberOfOrders):
        order(openHour, closeHour)
    incrementDate()
    global tmpListOfOrders
    tmpListOfOrders.sort(key=sortFn)
    
    global listOfOrders
    listOfOrders.extend(tmpListOfOrders.copy())
    tmpListOfOrders.clear()


# Function for week of orders
# Friday and Monday are the highest -> ifferent parameters for min and max orders
# Accounted for the closing times for Friday and Saturday being different from the rest
def orderWeek():
    ##Monday - busy
    orderDay(11, 22, minOrderForBusyDays, maxOrderForBusyDays)
    ##Tuesday
    orderDay(11, 22, minOrderForNormalDays, maxOrderForNormalDays)
    ##Wednesday
    orderDay(11, 22, minOrderForNormalDays, maxOrderForNormalDays)
    ##Thursday
    orderDay(11, 22, minOrderForNormalDays, maxOrderForNormalDays)
    ##Friday - busy
    orderDay(11, 23, minOrderForBusyDays, maxOrderForBusyDays)
    ##Saturday
    orderDay(11, 23, minOrderForNormalDays, maxOrderForNormalDays)
    ##Sunday
    orderDay(11, 22, minOrderForNormalDays, maxOrderForNormalDays)


# Function for all 52 weeks
def orderYear():
    for i in range(52):
        orderWeek()

# Function to write to csv
def writeToCSV():
    
    with open(filename, 'w', newline = '') as csvfile:
        # creating a csv writer object
        csvwriter = csv.writer(csvfile)

        ## drink names will be separated by comma for easier parsing later down the road
        ## time is separated by colon, in format hh:mm:ss
        ## date is separated by space, in format MM DD YYYY
        fields = ["Order ID", "Date", "Time", "Cost", "Drink Name(s)"]
        
        # writing the fields
        csvwriter.writerow(fields)
        
        # writing the data rows
        csvwriter.writerows(listOfOrders)

orderYear()
writeToCSV()

# Note: the 'orderID' in the csv file is not in order because entries are now sorted by time of order for a particular day
