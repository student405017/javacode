class test6 {
    public static void main(String[] args) {
        String[] names = {"小明", "小華", "小美", "小英","小王"};
        String[] schools = {"建中", "北一女", "建中", "成功高中","育達"};
        String[] genders = {"男", "男", "女", "女","男"};

        int sampleCount = names.length;
        int eventACount = 0;
        int eventBCount = 0;
        int intersectionCount = 0;

        for (int i = 0; i < names.length; i++) {
            boolean isA = schools[i].equals("建中");
            boolean isB = genders[i].equals("男");

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
        double probabilityBGivenA = (double) intersectionCount / eventACount;
        double probabilityAGivenB = (double) intersectionCount / eventBCount;
        double probabilityIntersectionByA = probabilityA * probabilityBGivenA;
        double probabilityIntersectionByB = probabilityB * probabilityAGivenB;

        System.out.println("交集（AND）：");
        System.out.println("P(A ∩ B) = P(A)P(B|A)");
        System.out.println("也可寫成 P(A ∩ B) = P(B)P(A|B)");
        System.out.println();

        System.out.println("樣本空間 S：所有學生");
        for (int i = 0; i < names.length; i++) {
            System.out.println(names[i] + "：" + schools[i] + "，" + genders[i]);
        }

        System.out.println();
        System.out.println("A：抽到建中學生");
        System.out.println("B：抽到男生");
        System.out.println("A ∩ B：抽到建中男生");
        System.out.println();

        System.out.println("n(S) = " + sampleCount);
        System.out.println("n(A) = " + eventACount);
        System.out.println("n(B) = " + eventBCount);
        System.out.println("n(A ∩ B) = " + intersectionCount);
        System.out.println();

        System.out.println("P(A) = " + probabilityA);
        System.out.println("P(B|A) = " + probabilityBGivenA);
        System.out.println("P(A ∩ B) = " + probabilityA + " * " + probabilityBGivenA);
        System.out.println("P(A ∩ B) = " + probabilityIntersectionByA);
        System.out.println();

        System.out.println("P(B) = " + probabilityB);
        System.out.println("P(A|B) = " + probabilityAGivenB);
        System.out.println("P(A ∩ B) = " + probabilityB + " * " + probabilityAGivenB);
        System.out.println("P(A ∩ B) = " + probabilityIntersectionByB);
    }
}
