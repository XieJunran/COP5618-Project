
JC = javac

target:
	${JC} ./src/cop5618/utility/*.java
	${JC} -classpath ./src ./src/cop5618/*.java


