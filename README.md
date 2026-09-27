The program accepts command-line arguments. The first argument is a mathematical expression, 
while subsequent arguments are variable assignments in the format "variable_name (as a single English letter) = real_number". 
The expression follows standard mathematical notation rules:
a unary minus must be preceded by an opening parenthesis if it follows another operator,
function arguments must be enclosed in parentheses,
exponentiation is performed right-to-left,
other mathematical operations are performed left-to-right,
uppercase and lowercase variable names are treated as distinct variables,
the expression may contain an unlimited number of variables.
Command-line arguments may include multiple values ​​for the same variable; 
values ​​for variables not present in the expression are ignored. 
If the expression contains multiple variables, the values ​​derived from the command-line arguments are stored in primitive lists collected into a single array. 
The program utilizes the Eclipse Collections library. 
Mathematically, this array of variable value lists represents a vector of values, where each sample is fed into the expression for calculation. 
If the samples are incomplete—for example, if the expression contains two variables but the arguments provide two values ​​for the first variable 
and three for the second—only the first two values ​​for each variable are paired into samples and substituted into the expression. 
The third value for the second variable is ignored, as there is insufficient data to complete the calculation. Upon completion, 
the calculator returns a result vector in the form of an array of real numbers. 
The program incorporates basic JUnit 5 testing skills and utilizes logging via the Log4j library. 
Additionally, it includes a `Graph` class that accepts a single-variable expression from the user 
and plots a graph where x-axis values ​​represent the variable's values ​​and y-axis values ​​represent the expression's results for each x. 
The XChart library is used for rendering the graph. You will find all the libraries used in the program in the `libs` folder.


