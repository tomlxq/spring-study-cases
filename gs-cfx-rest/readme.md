# 如果产生xml则这样
````aidl
    @GET
    @Path("")
    @Produces("application/xml")
    List<User> getUsers();
````

# 如果产生json则这样
````aidl
   @GET
    @Path("")
    @Produces("application/json")
    List<User> getUsers();
````
application.xml
````aidl
 <jaxrs:providers>
            <bean class="org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider"/>
        </jaxrs:providers>
````

* 解决soap ui   org.apache.http.conn.HttpHostConnectException: Connection to http://127.0.0.1:8888 refused

`Go to file > Preferences >Proxy settings and set Proxy to :None`