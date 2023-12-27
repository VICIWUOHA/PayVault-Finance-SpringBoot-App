package vicmicroservices.payvault;

import org.springframework.data.annotation.Id;


// The @Id annotation tells  Spring that the Id parameter is the ID field of this Class.
// Instead of using a regular class, we can use the Java Record object which is similar to the @dataclass in Python.
// This will automatically handle the getters and setters, hash, toString, etc. methods of a regular class object.
record PayCard(@Id Long Id, Double balance){

}
