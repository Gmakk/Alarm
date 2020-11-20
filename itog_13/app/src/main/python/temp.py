import numpy as np
import pandas as pd

training_set = pd.read_excel("/data/data/com.example.myapplication/files/Data_Train.xlsx")
test_set = pd.read_excel("/data/data/com.example.myapplication/files/Data_Test.xlsx")

training_set.head()

test_set.head()

###############################################################################################################################################

# chechking the features in the Datasets

###############################################################################################################################################


#Training Set

print("\nEDA on Training Set\n")
print("#"*30)
print("\nFeatures/Columns : \n", training_set.columns)
print("\n\nNumber of Features/Columns : ", len(training_set.columns))
print("\nNumber of Rows : ",len(training_set))
print("\n\nData Types :\n", training_set.dtypes)
print("\nContains NaN/Empty cells : ", training_set.isnull().values.any())
print("\nTotal empty cells by column :\n", training_set.isnull().sum(), "\n\n")


# Test Set
print("#"*30)
print("\nEDA on Test Set\n")
print("#"*30)
print("\nFeatures/Columns : \n",test_set.columns)
print("\n\nNumber of Features/Columns : ",len(test_set.columns))
print("\nNumber of Rows : ",len(test_set))
print("\n\nData Types :\n", test_set.dtypes)
print("\nContains NaN/Empty cells : ", test_set.isnull().values.any())
print("\nTotal empty cells by column :\n", test_set.isnull().sum())

###############################################################################################################################################

# Data Analysisng

###############################################################################################################################################


#Combining trainig set and test sets for analysing data and finding patterns

data_temp = [training_set[['DATE', 'TIME_DOWN', 'DAY', 'STEPS']], test_set]

data_temp = pd.concat(data_temp)




# Analysing CITY  DAY

all_cities = list(data_temp['DAY'])

for i in range(len(all_cities)):
    if type(all_cities[i]) == float:
        all_cities[i] = 'NOT AVAILABLE'
    all_cities[i] = all_cities[i].strip().upper()
        
print("\n\nNumber of Unique cities (Including NOT AVAILABLE): ", len(pd.Series(all_cities).unique()))
print("\n\nUnique Cities:\n", pd.Series(all_cities).unique())
 
all_cities = list(pd.Series(all_cities).unique())



###############################################################################################################################################

# Data Cleaning

###############################################################################################################################################


# Cleaning Training Set
#______________________



# Cleaning CITY  DAY

cities = list(training_set['DAY'])

for i in range(len(cities)):
    if type(cities[i]) == float:
        cities[i] = 'NOT AVAILABLE'
    cities[i] = cities[i].strip().upper()
        

    


# Votes  STEPS
       
votes = list(training_set['STEPS'])

for i in range(len(votes)) :
    try:
       votes[i] = int(votes[i].split(" ")[0].strip())
    except :
       pass       
    
    

new_data_train = {}

new_data_train['DATE'] = training_set["DATE"]
new_data_train['DAY'] = cities
new_data_train['STEPS'] = votes
new_data_train['TIME_UP'] = training_set["TIME_UP"]

new_data_train = pd.DataFrame(new_data_train)
#______________________



#______________________
# Cleaning Test Set
#______________________

# Cleaning CITY  DAY

cities = list(test_set['DAY'])

for i in range(len(cities)):
    if type(cities[i]) == float:
        cities[i] = 'NOT AVAILABLE'
    cities[i] = cities[i].strip().upper()
        

    


# Votes
       
votes = list(test_set['STEPS'])

for i in range(len(votes)) :
    try:
       votes[i] = int(votes[i].split(" ")[0].strip())
    except :
       pass       
    
    

new_data_test = {}

new_data_test['DATE'] = test_set["DATE"]
new_data_test['DAY'] = cities
new_data_test['STEPS'] = votes

new_data_test = pd.DataFrame(new_data_test)

print("\n\nnew_data_train: \n", new_data_train.head())
print("\n\nnew_data_test: \n", new_data_test.head())

#______________________

###############################################################################################################################################

# Data Preprocessing

###############################################################################################################################################

# Missing Values
#_______________

# Training Set

print("\n\nMissing Values in Training Set\n","#"*60)
print("\nContains NaN/Empty cells : ", new_data_train.isnull().values.any())
print("\nTotal empty cells by column\n","_"*60,"\n", new_data_train.isnull().sum())

new_data_train.fillna(0, inplace = True)

print("\n\nAfter Filling 0:\n","_"*60,"\n")
print("\nContains NaN/Empty cells : ", new_data_train.isnull().values.any())

# Test Set

print("\n\nMissing Values in Test Set \n","#"*60)
print("\nContains NaN/Empty cells : ", new_data_test.isnull().values.any())
print("\nTotal empty cells by column\n","_"*60,"\n", new_data_test.isnull().sum())


new_data_test.fillna(0, inplace = True)

print("\n\nAfter Filling 0 :\n","_"*60,"\n")
print("\nContains NaN/Empty cells : ", new_data_test.isnull().values.any())
print("\n\n")


# Encoding Categorical Variables
#_______________________________


from sklearn.preprocessing import LabelEncoder


le_city = LabelEncoder()




le_city.fit(all_cities)



# Training Set  




new_data_train['DAY'] = le_city.transform(new_data_train['DAY'])
# Test Set


new_data_test['DAY'] = le_city.transform(new_data_test['DAY'])


# Classifying Independent and Dependent Features
#_______________________________________________

# Dependent Variable
Y_train = new_data_train.iloc[:, -1].values  

# Independent Variables
X_train = new_data_train.iloc[:,0 : -1].values

# Independent Variables for Test Set
X_test = new_data_test.iloc[:,:].values


# Feature Scaling
#________________

from sklearn.preprocessing import StandardScaler

sc = StandardScaler()

X_train = sc.fit_transform(X_train)

X_test = sc.transform(X_test)


Y_train = Y_train.reshape((len(Y_train), 1)) 

Y_train = sc.fit_transform(Y_train)

Y_train = Y_train.ravel()

from sklearn.ensemble import GradientBoostingRegressor

gbr=GradientBoostingRegressor( loss = 'huber',learning_rate=0.001,n_estimators=350, max_depth=6
                              ,subsample=1,
                              verbose=False,random_state=126)   # Leaderboard SCORE :  0.8364249755816828 @ RS =126 ,n_estimators=350, max_depth=6

gbr.fit(X_train,Y_train)

y_pred_gbr = sc.inverse_transform(gbr.predict(X_test))

y_pred_gbr = pd.DataFrame(y_pred_gbr, columns = ['TIME_UP']) # Converting to dataframe
print(y_pred_gbr)

example_string = y_pred_gbr.to_string()
output_file = open('/data/data/com.example.myapplication/files/GradientBoostingRegressor.txt','a')
output_file.write(example_string)
output_file.close()
#y_pred_gbr.to_excel("GradientBoostingRegressor.xlsx", index = False ) # Saving the output in to an excel