package com.tom.demo.jdbc.framework;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.ArrayUtils;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

@Setter
@Getter
public class QueryRuleSqlBuilder {
    private int curIndex = 0;
    private List<String> properties;
    private List<Object> values;
    private List<Order> orders;
    private String whereSql = "";
    private String orderSql = "";
    private Object[] valueArr = new Object[]{};
    private Map<Object, Object> valueMap = Maps.newHashMap();

    public QueryRuleSqlBuilder(QueryRule queryRule) {
        curIndex = 0;
        properties = Lists.newArrayList();
        values = Lists.newArrayList();
        orders = Lists.newArrayList();
        for (QueryRule.Rule rule : queryRule.getRuleList()) {
            switch (rule.getType()) {
                case QueryRule.BETWEEN:
                    processBetween(rule);
                    break;
                case QueryRule.EQ:
                    processEqual(rule);
                    break;
                case QueryRule.LIKE:
                    processLike(rule);
                    break;
                case QueryRule.NOTEQ:
                    processNotEqual(rule);
                    break;
                case QueryRule.GT:
                    processGreaterThan(rule);
                    break;
                case QueryRule.GE:
                    processGreaterEqual(rule);
                    break;
                case QueryRule.LT:
                    processLessThan(rule);
                    break;
                case QueryRule.LE:
                    processLessEqual(rule);
                    break;
                case QueryRule.IN:
                    processIN(rule);
                    break;
                case QueryRule.NOTIN:
                    processNotIN(rule);
                    break;
                case QueryRule.ISNULL:
                    processIsNull(rule);
                    break;
                case QueryRule.ISNOTNULL:
                    processIsNotNull(rule);
                    break;
                case QueryRule.ISEMPTY:
                    processIsEmpty(rule);
                    break;
                case QueryRule.ISNOTEMPTY:
                    processIsNotEmpty(rule);
                    break;
                case QueryRule.ASC_ORDER:
                    processOrder(rule);
                    break;
                case QueryRule.DESC_ORDER:
                    processOrder(rule);
                    break;
                default:
                    throw new IllegalArgumentException("type " + rule.getType() + " not supported.");
            }
        }
        //拼装where语句
        appendWhereSql();
        //拼装排序语句
        appendOrderSql();
        //拼装参数值
        appendValues();
    }

    /**
     * 或得查询条件
     *
     * @return
     */
    public String getWhereSql() {
        return this.removeFirstAndForWhere(this.whereSql);
    }


    /**
     * 去掉order
     *
     * @param sql
     * @return
     */
    protected String removeOrders(String sql) {
        Pattern p = Pattern.compile("order\\s*by[\\w|\\W|\\s|\\S]*", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(sql);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, "");
        }
        m.appendTail(sb);
        return sb.toString();
    }

    /**
     * 去掉select
     *
     * @param sql
     * @return
     */
    protected String removeSelect(String sql) {
        if (sql.toLowerCase().matches("from\\s+")) {
            int beginPos = sql.toLowerCase().indexOf("from");
            return sql.substring(beginPos);
        } else {
            return sql;
        }
    }

    private void processLike(QueryRule.Rule rule) {
        if (!ArrayUtils.isEmpty(rule.getValues())) {
            return;
        }
        add(rule.getAndOr(), rule.getPropertyName(), "LIKE", rule.getValues()[0]);

    }

    private void processBetween(QueryRule.Rule rule) {
        if (ArrayUtils.isEmpty(rule.getValues()) || rule.getValues().length < 2) {
            return;
        }

        add(rule.getAndOr(), rule.getPropertyName(), "", "BETWEEN", rule.getValues()[0], "AND");
        add(0, "", "", "", rule.getValues()[1], "");

    }

    private void processEqual(QueryRule.Rule rule) {
        if (ArrayUtils.isEmpty(rule.getValues())) {
            return;
        }
        add(rule.getAndOr(), rule.getPropertyName(), "=", rule.getValues()[0]);
    }

    private void processNotEqual(QueryRule.Rule rule) {
        if (ArrayUtils.isEmpty(rule.getValues())) {
            return;
        }
        add(rule.getAndOr(), rule.getPropertyName(), "<>", rule.getValues()[0]);
    }

    private void processGreaterEqual(QueryRule.Rule rule) {
        if (ArrayUtils.isEmpty(rule.getValues())) {
            return;
        }
        add(rule.getAndOr(), rule.getPropertyName(), ">=", rule.getValues()[0]);
    }

    private void processGreaterThan(QueryRule.Rule rule) {
        if (ArrayUtils.isEmpty(rule.getValues())) {
            return;
        }
        add(rule.getAndOr(), rule.getPropertyName(), ">", rule.getValues()[0]);
    }

    private void processLessEqual(QueryRule.Rule rule) {
        if (ArrayUtils.isEmpty(rule.getValues())) {
            return;
        }
        add(rule.getAndOr(), rule.getPropertyName(), "<=", rule.getValues()[0]);
    }

    private void processLessThan(QueryRule.Rule rule) {
        if (ArrayUtils.isEmpty(rule.getValues())) {
            return;
        }
        add(rule.getAndOr(), rule.getPropertyName(), "<", rule.getValues()[0]);
    }

    private void processIsNotNull(QueryRule.Rule rule) {
        add(rule.getAndOr(), rule.getPropertyName(), "IS NOT", "NULL");
    }

    private void processIsNull(QueryRule.Rule rule) {
        add(rule.getAndOr(), rule.getPropertyName(), "IS", "NULL");
    }

    private void processIsNotEmpty(QueryRule.Rule rule) {
        add(rule.getAndOr(), rule.getPropertyName(), "!=", "\"\"");
    }

    private void processIsEmpty(QueryRule.Rule rule) {
        add(rule.getAndOr(), rule.getPropertyName(), "=", "\"\"");
    }

    /**
     * 处理in和not in
     *
     * @param rule
     * @param name
     */
    private void inAndNotIn(QueryRule.Rule rule, String name) {
        if (ArrayUtils.isEmpty(rule.getValues())) {
            return;
        }
        if ((rule.getValues().length == 1) && (rule.getValues()[0] != null)
                && (rule.getValues()[0] instanceof List)) {
            List<Object> list = (List) rule.getValues()[0];

            if ((list != null) && (list.size() > 0)) {
                for (int i = 0; i < list.size(); i++) {
                    if (i == 0 && i == list.size() - 1) {
                        add(rule.getAndOr(), rule.getPropertyName(), "", name + " (", list.get(i), ")");
                    } else if (i == 0 && i < list.size() - 1) {
                        add(rule.getAndOr(), rule.getPropertyName(), "", name + " (", list.get(i), "");
                    }
                    if (i > 0 && i < list.size() - 1) {
                        add(0, "", ",", "", list.get(i), "");
                    }
                    if (i == list.size() - 1 && i != 0) {
                        add(0, "", ",", "", list.get(i), ")");
                    }
                }
            }
        } else {
            Object[] list = rule.getValues();
            for (int i = 0; i < list.length; i++) {
                if (i == 0 && i == list.length - 1) {
                    add(rule.getAndOr(), rule.getPropertyName(), "", name + " (", list[i], ")");
                } else if (i == 0 && i < list.length - 1) {
                    add(rule.getAndOr(), rule.getPropertyName(), "", name + " (", list[i], "");
                }
                if (i > 0 && i < list.length - 1) {
                    add(0, "", ",", "", list[i], "");
                }
                if (i == list.length - 1 && i != 0) {
                    add(0, "", ",", "", list[i], ")");
                }
            }
        }
    }

    private void processNotIN(QueryRule.Rule rule) {
        inAndNotIn(rule, "not in");

    }

    private void processIN(QueryRule.Rule rule) {
        inAndNotIn(rule, "in");

    }

    private void processOrder(QueryRule.Rule rule) {
        switch (rule.getType()) {
            case QueryRule.ASC_ORDER:
                orders.add(Order.asc(rule.getPropertyName()));
                break;
            case QueryRule.DESC_ORDER:
                orders.add(Order.desc(rule.getPropertyName()));
                break;
            default:
                break;
        }

    }


    private void add(int andOr, String key, String split, Object value) {
        add(andOr, key, split, "", value, "");
    }


    /**
     * @param andOr  AND or OR
     * @param key    字段名
     * @param split
     * @param prefix
     * @param value
     * @param suffix
     */
    private void add(int andOr, String key, String split, String prefix, Object value, String suffix) {
        String andOrStr = 0 == andOr ? "" : (QueryRule.AND == andOr ? " AND " : " OR ");
        properties.add(curIndex, andOrStr + key + " " + split + prefix + (null != value ? " ? " : " ") + suffix);
        if (null != value) {
            values.add(curIndex, value);
            curIndex++;
        }
    }

    private void appendValues() {
        Object[] val = new Object[values.size()];
        IntStream.range(0, values.size())
                .forEach(idx -> {
                            val[idx] = values.get(idx);
                            valueMap.put(idx, val[idx]);
                        }
                );


        this.valueArr = val;

    }

    private void appendOrderSql() {
        StringBuffer stringBuffer = new StringBuffer();
        IntStream.range(0, orders.size()).forEach(idx -> {
                    if (idx > 0 && idx < orders.size()) {
                        stringBuffer.append(",");
                    }
                    stringBuffer.append(orders.get(idx).toString());

                }
        );
        this.orderSql = removeSelect(removeOrders(stringBuffer.toString()));


    }

    private void appendWhereSql() {
        StringBuffer stringBuffer = new StringBuffer();
        properties.forEach(p -> {
            stringBuffer.append(p);
        });
        this.whereSql = removeSelect(removeOrders(stringBuffer.toString()));

    }

    /**
     * 处理
     *
     * @param sql
     * @return
     */
    private String removeFirstAndForWhere(String sql) {
        if (null == sql) {
            return sql;
        }
        return sql.trim().toLowerCase().replaceAll("^\\s*and", "") + " ";
    }

}
