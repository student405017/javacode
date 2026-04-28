class test5 {
    public static void main(String[] args) {
        String[] names = {"小明", "小華", "小美", "小英","小王"};
        String[] schools = {"建中", "北一女", "建中", "成功高中","育達"};
        String[] genders = {"男", "男", "女", "女","男"};

        int sampleCount = names.length;
        int eventACount = 0;
        int eventBCount = 0;
        int intersectionCount = 0;
        int unionCount = 0;

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

            if (isA || isB) {
                unionCount++;
            }
        }

        double probabilityA = (double) eventACount / sampleCount;
        double probabilityB = (double) eventBCount / sampleCount;
        double probabilityIntersection = (double) intersectionCount / sampleCount;
        double probabilityUnion = probabilityA + probabilityB - probabilityIntersection;

        System.out.println("聯集（OR）：");
        System.out.println("P(A ∪ B) = P(A) + P(B) - P(A ∩ B)");
        System.out.println("若互斥：P(A ∪ B) = P(A) + P(B)");
        System.out.println();

        System.out.println("樣本空間 S：所有學生");
        for (int i = 0; i < names.length; i++) {
            System.out.println(names[i] + "：" + schools[i] + "，" + genders[i]);
        }

        System.out.println();
        System.out.println("A：抽到建中學生");
        System.out.println("B：抽到男生");
        System.out.println("A ∪ B：抽到建中學生或男生");
        System.out.println("A ∩ B：抽到建中男生");
        System.out.println();

        System.out.println("n(S) = " + sampleCount);
        System.out.println("n(A) = " + eventACount);
        System.out.println("n(B) = " + eventBCount);
        System.out.println("n(A ∩ B) = " + intersectionCount);
        System.out.println("n(A ∪ B) = " + unionCount);
        System.out.println();

        System.out.println("P(A) = " + probabilityA);
        System.out.println("P(B) = " + probabilityB);
        System.out.println("P(A ∩ B) = " + probabilityIntersection);
        System.out.println("P(A ∪ B) = " + probabilityA + " + " + probabilityB + " - " + probabilityIntersection);
        System.out.println("P(A ∪ B) = " + probabilityUnion);
    }
}
