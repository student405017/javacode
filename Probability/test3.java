class test3 {
    public static void main(String[] args) {
        int eventCount = 2;  // n(A)：事件 A 發生的個數
        int sampleCount = 4; // n(S)：樣本空間中全部可能結果的個數

        double probability = (double) eventCount / sampleCount;

        System.out.println("基本公式：");
        System.out.println("P(A) = n(A) / n(S)");
        System.out.println();

        System.out.println("n(A) = " + eventCount);
        System.out.println("n(S) = " + sampleCount);
        System.out.println("P(A) = " + eventCount + " / " + sampleCount);
        System.out.println("P(A) = " + probability);
    }
}
