from random import Random
import csv

##Function for one order
def order():
    ##need to generate number of drinks and drink number 1-19


    ##time of order: 
    ##payment from drink number based off array of the recipe prices


##Function for day of orders



##Function for week of orders



##Function for all 52 weeks, make sure to generate unique orderID


##Function to write to csv
def writeToCSV(){
    filename = "OrderHistory.csv"

    with open(filename, 'w') as csvfile:
        # creating a csv writer object
        csvwriter = csv.writer(csvfile)
        
        # writing the fields
        csvwriter.writerow(columns)
        
        # writing the data rows
        csvwriter.writerows(rows)
}