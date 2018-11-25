public class MaxNumAdd {
    static final StringBuffer _a = new StringBuffer("immutable");

    public static void main(String[] args) {
        int a = Integer.MAX_VALUE;

        int b = Integer.MAX_VALUE;

        int sum = a + b;

        System.out.println("a=" + a + ",b=" + b + ",sum=" + sum);


        System.out.println(_a.append(" broken!"));

        String username = null;
       /* if(username.equals("zxx")){
            System.out.println(_a.append("zxx"));
        }*/
        if ("zxx".equals(username)) {
            System.out.println(_a.append("zxx"));
        }
    }
}
