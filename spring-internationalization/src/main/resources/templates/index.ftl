


<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8" />
    <title>HELLO</title>
</head>
<body>
hello,world
<select name="lang" onchange="location.href='?lang='+this.options[selectedIndex].value">
    <option value="en_US">en_US</option>
    <option value="zh_CN">zh_CN</option>
</select>

<form id="gizmo-form" role="form"  action="/save" method="post">
    <div>
        <label for="field1" ><@spring.message "field1"/></label>
        <input type="text" id="field1" name="field1" value="<@spring.message "field1"/>" />
    </div>
    <div>
        <label for="field2"><@spring.message "field2"/></label>
        <input type="text" id="field2" name="field2" value="<@spring.message "field2"/>" />
    </div>
    <div>
        <ul>
          <#--  <li th:each="item, stat : *{children}" class="itemRow">
                <div>
                    <label th:for="${'childField1-'+stat.index}">Field 1</label>
                    <input type="text" class="form-control quantity" name="childField1"
                           th:field="*{children[__${stat.index}__].childField1}" th:id="${'childField1-'+stat.index}"/>

                    <label th:for="${'childField2-'+stat.index}">Field 2</label>
                    <input type="text" class="form-control quantity" name="childField2"
                           th:field="*{children[__${stat.index}__].childField2}" th:id="${'childField2-'+stat.index}"/>
                </div>
            </li>-->
        </ul>
    </div>
    <div>
        <button type="submit">Save</button>
    </div>
</form>
</body>
</html>