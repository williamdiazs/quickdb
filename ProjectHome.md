![http://quickdb.googlecode.com/files/quickDBLogo.png](http://quickdb.googlecode.com/files/quickDBLogo.png)
<br><font color='#6495ed' face='Verdana'>
<h2>Version 1.2 RELEASED!</h2>
</font>
<font color='#3CB371' face='Verdana'>
<h2>Version 1.3 ALMOST HERE!</h2>
<i>Delayed until the release of <a href='http://code.google.com/p/ninja-ide/'>NINJA-IDE</a> Version 1.0</i><br />
<i><a href='http://ninja-ide.googlecode.com'>http://ninja-ide.googlecode.com</a></i>
<br />
</font>

<i>Working with Databases has never been Easier!</i>

QuickDB aims to develop a persistence library that allow the user to write only the Data Model, and the library will manage all the operations between the Entities and the Database, without writing any line of code for Connection, Sql, etc.<br>
<br>
<b>Where each operation involves only what you wanna do (save, modify, ...) and to what are you going to apply it (the object)</b>

<table><thead><th> The Source Code, the <i>Jar</i> File and a Site with several Reports can be downloaded from:<br><a href='http://quickdb.googlecode.com/files/QuickDB-1.2.zip'>QuickDB-1.2</a><br><strong>QuickDB 1.3-Beta2:</strong><br><a href='http://quickdb.googlecode.com/files/QuickDB-1.3-Beta2.zip'>QuickDB-1.3-Beta2</a><br><br><strong>Documentation for Version 1.2</strong><br><i>New Features of Version 1.3 not added in the documentation yet</i><br><strong>Spanish Tutorial:</strong><br><a href='http://code.google.com/p/quickdb/wiki/TutorialSpanish'>Tutorial HTML</a><br><a href='http://quickdb.googlecode.com/files/tutorialQuickDB1.2.pdf'>Tutorial PDF</a><br><br><strong>English Tutorial:</strong><br><a href='http://code.google.com/p/quickdb/wiki/TutorialEnglish'>Tutorial HTML</a><br><a href='http://quickdb.googlecode.com/files/tutorialQuickDB1.2-En.pdf'>Tutorial PDF</a><br><br><strong><a href='http://code.google.com/p/quickdb/wiki/mavenRepo'>Using QuickDB with Maven</a></strong> <br><br><a href='http://sites.google.com/site/quickdbdeveloper/release-notes'>Release Notes</a></th></thead><tbody></tbody></table>

<a href='Hidden comment: 
<font color="#6495ed" face="Verdana">
===January 10, 2010: Version 1.2===


Unknown end tag for </font>


'></a><br>
<i>(Working in a Python, .NET, PHP and Mobile{J2ME, .NET CF} Version)</i><br>
<i>Working to add support for SQL Server</i>


<br><font color='#191970' face='Verdana'>
<h1>Why QuickDB?</h1>
</font>
<i>Functionality</i> is not synonymous with <i>Complexity</i>.<br>
Being able to do a lot of things don't have to involve waste a lot of time in configuration tasks.<br>
<br>
<br><font color='#191970' face='Verdana'>
<h1>Capabilities</h1>
</font>

<img src='http://quickdb.googlecode.com/files/capabilities1-2.png' />

<blockquote>This capabilities can be combined with Inheritance, Compound Objects, Collections (Many to Many Relation, One to Many Relation) and Automatic Table Creation.<br>
<i>(saveAll and modifyAll receive a Collection of Objects)</i><br>
<i>(obtainAll return a Collection of Objects)</i></blockquote>

<br><font color='#191970' face='Verdana'>
<h1>Keep It Simple...</h1>
</font>
<b>Example Classes:</b>

<img src='http://quickdb.googlecode.com/files/ex-classes.png' />

<pre><code>//Create instance of AdminBase<br>
//Parameters: DBMS, HOST, PORT, DB, USER, PASSWORD<br>
AdminBase admin = new AdminBase(AdminBase.DATABASE.MYSQL, "localhost",<br>
        "3306", "exampleQuickDB", "root", "");<br>
<br>
//Create Address Object<br>
Address a = new Address();<br>
a.setNumber(123);<br>
a.setStreet("unnamed street");<br>
<br>
//Create Collection of Phones<br>
Phone p1 = new Phone();<br>
p1.setAreaCode("351");<br>
p1.setNumber("123456");<br>
Phone p2 = new Phone();<br>
p2.setAreaCode("351");<br>
p2.setNumber("4567890");<br>
ArrayList&lt;Phone&gt; phones = new ArrayList&lt;Phone&gt;();<br>
phones.add(p1);<br>
phones.add(p2);<br>
<br>
//Create Employe Object<br>
Employee e = new Employee();<br>
e.setCode(555);<br>
e.setRolDescription("play ping pong");<br>
e.setName("Diego Sarmentero");<br>
e.setBirth(new java.sql.Date(100, 4, 20));<br>
e.setAddress(a);<br>
e.setPhone(phones);<br>
</code></pre>

<h3>Operation: Save</h3>
<pre><code>admin.save(e); //Save Employe<br>
</code></pre>
<h3>Result: Save (Tables)</h3>
<i>Create the Tables automatically</i>

<b>Address</b>
<table><thead><th> id </th><th> street </th><th> number </th></thead><tbody>
<tr><td> 1  </td><td> unnamed street </td><td> 123    </td></tr></tbody></table>

<b>Phone</b>
<table><thead><th> id </th><th> areaCode </th><th> number </th></thead><tbody>
<tr><td> 1  </td><td> 351      </td><td> 123456 </td></tr>
<tr><td> 2  </td><td> 351      </td><td> 4567890 </td></tr></tbody></table>

<b>Person</b>
<table><thead><th> id </th><th> name </th><th> birth </th><th> address </th></thead><tbody>
<tr><td> 1  </td><td> Diego Sarmentero </td><td> 2000-05-20 </td><td> 1       </td></tr></tbody></table>

<b>PersonPhonePhone</b>
<table><thead><th> id </th><th> base </th><th> related </th></thead><tbody>
<tr><td> 1  </td><td> 1    </td><td> 1       </td></tr>
<tr><td> 2  </td><td> 1    </td><td> 2       </td></tr></tbody></table>

<b>Employee</b>
<table><thead><th> id </th><th> code </th><th> rolDescription </th><th> parent_id </th></thead><tbody>
<tr><td> 1  </td><td> 555  </td><td> play ping pong </td><td> 1         </td></tr></tbody></table>

<h3>Operation: Obtain</h3>
Get the Employee object where the inherited attribute "address" has as street value = 'unnamed street'<br>
<i>(QuickDB has a Query System 100% Object Oriented)</i>
<pre><code>admin.obtain(e, "address.street = 'unnamed street'");<br>
</code></pre>
Or<br>
<pre><code>admin.obtain(e).If("street", Address.class).equal("unnamed street").find();<br>
</code></pre>

<h3>Operation: Modify</h3>
Change Employee name and add a new Phone (after obtain the object).<br>
<pre><code>e.setName("Leonardo");<br>
Phone p = new Phone();<br>
p.setAreaCode("123");<br>
p.setNumber("98765");<br>
e.getPhone().add(p);<br>
<br>
admin.modify(e);<br>
</code></pre>

<h3>Operation: Delete</h3>
Delete this Employee object (after obtain).<br>
<pre><code>admin.delete(e);<br>
</code></pre>

<br><font color='#6495ed' face='Verdana'>
<h2>Is it really so Simple??:</h2>
</font>
<a href='http://www.youtube.com/watch?feature=player_embedded&v=cVPsxUP4Wa8' target='_blank'><img src='http://img.youtube.com/vi/cVPsxUP4Wa8/0.jpg' width='800' height=480 /></a><br>
<br>
<hr />
To participate or request features please contact: quickdb@googlegroups.com<br>
<hr />
<img src='http://quickdb.googlecode.com/files/maven-feather.png' />