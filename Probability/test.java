class test {
    public static void main(String[] args) {
        int eventCount = 3;   // 事件 A 發生的個數，例如骰子擲出偶數：2、4、6
        int totalCases = 6;   // 全部可能情況，例如骰子有 1 到 6 點

        double probability = (double) eventCount / totalCases;

        System.out.println("機率的定義：");
        System.out.println("P(A) = 事件 A 發生的個數 / 全部可能情況");
        System.out.println("P(A) = " + eventCount + " / " + totalCases);
        System.out.println("P(A) = " + probability);
    }
}
