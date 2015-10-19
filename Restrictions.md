# 2: #
# Restrictions #

To work with QuickDB, there are certain restrictions that must be borne in mind:

  * All model classes should contain an integer primary key attribute, and should be the first attribute declared in the class.
  * All model classes should contain an empty constructor.
  * In case of a Parent-Son relation, if "Get" and "Set" methods of the primary key have the same name, then both Parent and Son must have the same primary key value (since they are public methods, parent's value would be overwrtten by the son, preventing another class to be stored in the database extending from the same parent. Is solved using Annotations).
  * To combine the use of attributes in a class with and without annotations (to set the table creation, ie when using the annotation @ColumnDefinition), you need to add the annotation @Table to Class.