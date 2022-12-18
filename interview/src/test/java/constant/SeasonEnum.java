package constant;

/**
 *
 * @ClassName: qqq
 * @Description:
 * @Author: tomluo
 * @Date: 2022/12/17 15:53
 **/
public enum SeasonEnum {
    SPRING(1), SUMMER(2), AUTUMN(3), WINTER(4);

    int seq;

    SeasonEnum(int seq) {
        this.seq = seq;
    }
}