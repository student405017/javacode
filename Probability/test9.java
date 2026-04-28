class test9 {
    public static void main(String[] args) {
        double probabilityA = 0.01;           // P(A)：真的生病的機率
        double probabilityNotA = 1 - probabilityA;
        double probabilityBGivenA = 0.95;     // P(B|A)：生病時，檢測陽性的機率
        double probabilityBGivenNotA = 0.05;  // P(B|A^c)：沒生病時，檢測陽性的機率

        double probabilityB = probabilityBGivenA * probabilityA
                + probabilityBGivenNotA * probabilityNotA;
        double probabilityAGivenB = probabilityBGivenA * probabilityA / probabilityB;

        System.out.println("貝氏定理：");
        System.out.println("P(A|B) = P(B|A)P(A) / P(B)");
        System.out.println("用途：已知結果，反推原因");
        System.out.println();

        System.out.println("A：真的生病");
        System.out.println("B：檢測結果為陽性");
        System.out.println();

        System.out.println("P(A) = " + probabilityA);
        System.out.println("P(A^c) = " + probabilityNotA);
        System.out.println("P(B|A) = " + probabilityBGivenA);
        System.out.println("P(B|A^c) = " + probabilityBGivenNotA);
        System.out.println();

        System.out.println("P(B) = P(B|A)P(A) + P(B|A^c)P(A^c)");
        System.out.println("P(B) = " + probabilityB);
        System.out.println();

        System.out.println("P(A|B) = P(B|A)P(A) / P(B)");
        System.out.println("P(A|B) = " + probabilityBGivenA + " * " + probabilityA + " / " + probabilityB);
        System.out.println("P(A|B) = " + probabilityAGivenB);
    }
}
