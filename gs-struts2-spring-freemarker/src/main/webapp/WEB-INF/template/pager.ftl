<#-- 自定义的分页指令 (powered by TOM on 2012-07-09)
    属性：
   pageNo      define current page number
   recordCount total records
   pageSize   option, in java code ,page offset must be the same, default 10
   first_page_text option,define first page name
   previous_page_text option, define previous page name,
   next_page_text option, define next page name,
   last_page_text option, define last page name
 -->
<#macro p page count  pageSize=10 first_page_text="First" previous_page_text="Previous" next_page_text="Next" last_page_text="Last">
    <#assign current_page=page?number>
    <#assign recordCount=count?number>
    <#if pageSize <= 0>
        <#assign pageSize=10>
    </#if>
<#-- 定义局部变量pageCount保存总页数 -->
    <#assign pageCount=((recordCount + pageSize - 1) / pageSize)?int>
    <#if recordCount==0><#return/></#if>
<#-- 页号越界处理 -->
    <#if (current_page > pageCount)>
        <#assign current_page=pageCount>
    </#if>
    <#if (current_page < 1)>
        <#assign current_page=1>
    </#if>


<style type="text/css">


    /* 网易风格*/
    .anpager{width: auto; margin: 0 auto}
    .anpager .cpb {background:#1F3A87 none repeat scroll 0 0;border:1px solid #CCCCCC;color:#FFFFFF;font-weight:bold;margin:5px 4px 0 0;padding:4px 5px 0;}
    .anpager a {background:#FFFFFF none repeat scroll 0 0;border:1px solid #CCCCCC;color:#1F3A87;margin:5px 4px 0 0;padding:4px 5px 0;text-decoration:none}
    .anpager a:hover{background:#1F3A87 none repeat scroll 0 0;border:1px solid #1F3A87;color:#FFFFFF;}

    /* 拍拍网风格*/
    .paginator { font: 11px Arial, Helvetica, sans-serif;padding:10px 20px 10px 0; /*margin: 0px;*/width: auto; margin: 0 auto}
    .paginator a {padding: 1px 6px; border: solid 1px #ddd; background: #fff; text-decoration: none;margin-right:2px}
    .paginator a:visited {padding: 1px 6px; border: solid 1px #ddd; background: #fff; text-decoration: none;}
    .paginator .cpb {padding: 1px 6px;font-weight: bold; font-size: 13px;border:none}
    .paginator a:hover {color: #fff; background: #ffa501;border-color:#ffa501;text-decoration: none;}

    /* 迅雷风格*/
    .pages { color: #999;width:auto; margin: 0 auto }
    .pages a, .pages .cpb { text-decoration:none;float: left; padding: 0 5px; border: 1px solid #ddd;background: #fffff;margin:0 2px; font-size:11px; color:#000;}
    .pages a:hover { background-color: #E61636; color:#fff;border:1px solid #E61636; text-decoration:none;}
    .pages .cpb { font-weight: bold; color: #fff; background: #E61636; border:1px solid #E61636;}
    .pagination {clear: left;overflow: hidden;float: right;}
</style>
<div class="pagination">
    <@show_style style_name="anpager" recordCount=recordCount current_page=current_page pageCount=pageCount first_page_text=first_page_text previous_page_text=previous_page_text next_page_text=next_page_text last_page_text=last_page_text/></div>
<#--<@show_style style_name="paginator" recordCount=recordCount current_page=current_page pageCount=pageCount />
<@show_style style_name="pages" recordCount=recordCount current_page=current_page pageCount=pageCount />-->
</#macro>
<#macro show_style style_name recordCount current_page pageCount first_page_text previous_page_text next_page_text last_page_text>

<#-- 输出分页表单 -->
<#--<p>
<div id="pagination-bottom" class="pagination clearfix">
    <div class="pos-left"> Page 1 of ${pageCount?string("0")}    </div>
    <div class="pos-right">-->
<div class="${style_name}">
    <#if (current_page == 1)>
        <a disabled="disabled" <#--style="margin-right:5px;"-->>${first_page_text}</a>
    <#else><a <#--href='javascript:goPage("${current_page}",1);'--> href="javascript:void(0);" onclick="return false;"
                                                                    id="goPage_${current_page}_1" <#--style="margin-right:5px;"-->>${first_page_text}</a>
    </#if>
    <#if (current_page == 1)>
        <a disabled="disabled" <#--style="margin-right:5px;"-->>${previous_page_text}</a>
    <#else><a <#--href='javascript:goPage("${current_page}","${(current_page - 1)?string("0")}");'-->
            href="javascript:void(0);" onclick="return false;"
            id="goPage_${current_page}_${(current_page - 1)?string("0")}" <#--style="margin-right:5px;"-->>${previous_page_text}</a>
    </#if>
    <#assign start=1>
    <#if pageCount lte 10>
        <#assign end=pageCount>
    <#else>
        <#assign start=current_page-10+1>

        <#if start lt 0>         <#assign start=1>   </#if>
        <#assign end=start+10-1>
        <#if end gt pageCount>
            <#assign start=pageCount>   </#if>
    </#if>

    <#if (start gt 1)&&(pageCount gt 10)>
        <a <#--href='javascript:goPage("${current_page}","${(start - 1)?string("0")}");' --> href="javascript:void(0);" onclick="return false;"
                                                                                             id="goPage_${current_page}_${(start - 1)?string("0")}" <#--style="margin-right:5px;"-->>...</a>
    </#if>




    <#list start..end as i>
        <#if (current_page==i)> <span class="cpb" <#--style="margin-right:5px;"-->>${i?string("0")}</span>
        <#else>  <a <#--href='javascript:goPage("${current_page}","${i?string("0")}");'--> href="javascript:void(0);" onclick="return false;"
                                                                                           id="goPage_${current_page}_${i?string("0")}" <#--style="margin-right:5px;"-->>${i?string("0")}</a>
        </#if>
    </#list>




    <#if ((end+1) lt pageCount)&&(pageCount gt 10)>
        <a <#--href='javascript:goPage("${current_page}","${(end + 1)?string("0")}");'--> href="javascript:void(0);" onclick="return false;"
                                                                                          id="goPage_${current_page}_${(end + 1)?string("0")}"  <#--style="margin-right:5px;"-->>...</a>
    </#if>


    <#if (current_page == pageCount)>
        <a disabled="disabled" <#--style="margin-right:5px;"-->>${next_page_text}</a>
    <#else>
        <a <#--href='javascript:goPage("${current_page}","${(current_page + 1)?string("0")}");'--> href="javascript:void(0);" onclick="return false;"
                                                                                                   id="goPage_${current_page}_${(current_page + 1)?string("0")}"
        <#--style="margin-right:5px;"-->>${next_page_text}</a>
    </#if>
    <#if (current_page == pageCount)>   <a disabled="disabled"
    <#--style="margin-right:5px;"-->>${last_page_text}</a> <#else>
        <a <#--href='javascript:goPage("${current_page}","${pageCount?string("0")}");'-->  href="javascript:void(0);" onclick="return false;"
                                                                                           id="goPage_${current_page}_${pageCount?string("0")}"
        <#--style="margin-right:5px;"-->>${last_page_text}</a>     </#if>
</div>

<#-- </div><div class="page-skip">
     Go to Page
     <input id="pagination-bottom-input" class="pagination-input" type="text" value="">
     <input id="pagination-bottom-goto" class="pagination-goto" type="button" value="Go"
            onclick="location.href='?page='+document.getElementById("pagination-bottom-input").value +'${requestParams}'">
    </div>
</div>    </p>-->
</#macro>