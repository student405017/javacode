class test10 {
    public static void main(String[] args) {
        String[] groups = {"B1：建中學生", "B2：北一女學生", "B3：其他學生"};
        double[] probabilityB = {0.4, 0.35, 0.25};       // P(Bi)
        double[] probabilityAGivenB = {0.7, 0.6, 0.2};   // P(A|Bi)

        double probabilityA = 0;

        System.out.println("全機率定理：");
        System.out.println("P(A) = Σ P(A|Bi)P(Bi)");
        System.out.println("意思：把所有可能情況 Bi 分開計算，再全部加總");
        System.out.println();

        System.out.println("A：抽到成績及格的學生");
        System.out.println("Bi：學生所屬的不同群組");
        System.out.println();

        for (int i = 0; i < groups.length; i++) {
            double term = probabilityAGivenB[i] * probabilityB[i];
            probabilityA += term;

            System.out.println(groups[i]);
            System.out.println("P(B" + (i + 1) + ") = " + probabilityB[i]);
            System.out.println("P(A|B" + (i + 1) + ") = " + probabilityAGivenB[i]);
            System.out.println("P(A|B" + (i + 1) + ")P(B" + (i + 1) + ") = " + term);
            System.out.println();
        }

        System.out.println("P(A) = Σ P(A|Bi)P(Bi)");
        System.out.println("P(A) = " + probabilityA);
    }
}
