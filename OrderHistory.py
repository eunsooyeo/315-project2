
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
date = "2023/01/01"

filename = "OrderHistory.csv"

# 'listOfOrders' keeps track of the whole order, it is a list of list
# 'tmpListOfOrders' keeps track of the order for a day and sorts it by time (hours, minutes, seconds)
listOfOrders = []
tmpListOfOrders = []

monthsWith31Days = {"01", "03", "05", "07", "08", "10", "12"}
months = ["01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"]

# Busy days are set to be Monday and Friday currently, the two peak days 
minOrderForNormalDays = 200
maxOrderForNormalDays = 400
minOrderForBusyDays = 300
maxOrderForBusyDays = 500

# Increments date, switch to new month when necessary
# Uses the set 'monthsWith31Days' and list 'months' for conveniency
def incrementDate():
    global date
    arr = date.split("/")
    if(arr[1] in monthsWith31Days):
        if(int(arr[2]) == 31):
            if(months.index(arr[1]) == 11):
                date = "2024/01/01"
            else:
                date =arr[0] + "/" + months[months.index(arr[1]) + 1] + "/01"
        else:
            if ((int(arr[2]) + 1) < 10):
                date = arr[0] + "/" + arr[1] + "/0" +  str(int(arr[2]) + 1)
            else:
                date = arr[0] + "/" + arr[1] + "/" +  str(int(arr[2]) + 1)
    else:
        if(arr[1] == "02" and int(arr[2]) == 28):
            date = "2023/03/01"
        elif(int(arr[2]) == 30):
            date = arr[0] + "/" + months[months.index(arr[1]) + 1] + "/01"
        else:
            if ((int(arr[2]) + 1) < 10):
                date = arr[0] + "/" + arr[1] + "/0" + str(int(arr[2]) + 1)
            else:
                date = arr[0] + "/" + arr[1] + "/" + str(int(arr[2]) + 1)

# Generate a random number of drinks for the order with 1 drink having the highes probability and decreasing
def generateNumberOfDrinks() -> int: 
    num = random.randint(1, 100)
    # 1 drink: 1 - 70
    # 2 drinks: 71 - 90
    # 3 drinks: 91 - 95
    # 4 drinks: 95 - 97
    # 5 drinks: 98 - 99
    # 6 drinks: 100
    if(num <= 70):
        return 1
    if(num <= 90):
        return 2
    if(num <= 95):
        return 3
    if(num <= 97):
        return 4
    if(num <= 99):
        return 5
    return 6

# Function for one order
def order(openHour, closeHour):
    orderList = []

    ##need to generate number of drinks and drink number 1-19
    numberOfDrinks = generateNumberOfDrinks()
    drinkNumbers = []
    drinkNumbersStr = "{"
    for i in range(numberOfDrinks):
        num = random.randint(1, 19)
        drinkNumbers.append(num)
        drinkNumbersStr = drinkNumbersStr + str(num) + ", "
    drinkNumbersStr = drinkNumbersStr[0: -2]
    drinkNumbersStr += "}"
    orderList.append(drinkNumbersStr)
    
    orderList.append(date)

    hour = random.randint(openHour, closeHour)
    min = random.randint(0, 59)
    sec = random.randint(0, 59)
    time = str(hour) + ":"

    if(min < 10):
        time += "0" + str(min) + ":"
    else:
        time += str(min) + ":"
    if(sec < 10):
        time += "0" + str(sec)
    else:
        time += str(sec)
    orderList.append(time)

    cost = 0.0
    for i in drinkNumbers:
        cost += round(float(drinkAndPrice[i-1][1]),2)

    orderList.append(round(cost,2))
    
    global tmpListOfOrders
    tmpListOfOrders.append(orderList)

# Function that sorts based on the 3rd column, used in orderDay
def sortFn(list):
    return list[2]

# Function for day of orders
def orderDay(openHour, closeHour, minOrder, maxOrder):
    numberOfOrders = random.randint(minOrder, maxOrder)

    ## create two peak days: friday of start of semesters: 2023/08/25 & 2023/01/20
    if(date == "2023/08/25"):
        numberOfOrders = 530
    if(date == "2023/01/20"):
        numberOfOrders = 520

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
        # fields = ["Order ID", "Date", "Time", "Cost", "Drink ID(s)"]
        fields = ["Drink ID(s)", "Date", "Time", "Cost"]
        
        # writing the fields
        csvwriter.writerow(fields)
        
        # writing the data rows
        csvwriter.writerows(listOfOrders)

orderYear()
writeToCSV()

# Note: the 'orderID' in the csv file is not in order because entries are now sorted by time of order for a particular day
