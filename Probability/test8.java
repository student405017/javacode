class test8 {
    public static void main(String[] args) {
        String[] sampleSpace = {"HH", "HT", "TH", "TT"};

        int sampleCount = sampleSpace.length;
        int eventACount = 0;
        int eventBCount = 0;
        int intersectionCount = 0;

        for (int i = 0; i < sampleSpace.length; i++) {
            boolean isA = sampleSpace[i].charAt(0) == 'H';
            boolean isB = sampleSpace[i].charAt(1) == 'H';

            if (isA) {
                eventACount++;
            }

            if (isB) {
                eventBCount++;
            }

            if (isA && isB) {
                intersectionCount++;
            }
        }

        double probabilityA = (double) eventACount / sampleCount;
        double probabilityB = (double) eventBCount / sampleCount;
        double probabilityIntersection = (double) intersectionCount / sampleCount;
        double probabilityProduct = probabilityA * probabilityB;
        double probabilityAGivenB = probabilityIntersection / probabilityB;

        System.out.println("獨立事件：");
        System.out.println("若 A、B 獨立");
        System.out.println("P(A ∩ B) = P(A)P(B)");
        System.out.println("且 P(A|B) = P(A)");
        System.out.println();

        System.out.println("樣本空間 S：丟兩枚硬幣");
        System.out.println("H 代表正面，T 代表反面");
        System.out.print("S = { ");

        for (int i = 0; i < sampleSpace.length; i++) {
            System.out.print(sampleSpace[i]);

            if (i < sampleSpace.length - 1) {
                System.out.print(", ");
            }
        }

        System.out.println(" }");
        System.out.println();

        System.out.println("A：第一枚硬幣是正面");
        System.out.println("B：第二枚硬幣是正面");
        System.out.println("A ∩ B：兩枚硬幣都是正面");
        System.out.println();

        System.out.println("n(S) = " + sampleCount);
        System.out.println("n(A) = " + eventACount);
        System.out.println("n(B) = " + eventBCount);
        System.out.println("n(A ∩ B) = " + intersectionCount);
        System.out.println();

        System.out.println("P(A) = " + probabilityA);
        System.out.println("P(B) = " + probabilityB);
        System.out.println("P(A ∩ B) = " + probabilityIntersection);
        System.out.println("P(A)P(B) = " + probabilityA + " * " + probabilityB);
        System.out.println("P(A)P(B) = " + probabilityProduct);
        System.out.println();

        System.out.println("P(A|B) = P(A ∩ B) / P(B)");
        System.out.println("P(A|B) = " + probabilityIntersection + " / " + probabilityB);
        System.out.println("P(A|B) = " + probabilityAGivenB);
    }
}
