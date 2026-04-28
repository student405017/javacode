class test4 {
    public static void main(String[] args) {
        String[] names = {"小明", "小華", "小美", "小英","小王"};
        String[] schools = {"建中", "北一女", "建中", "成功高中","成功高中"};

        int sampleCount = names.length;
        int eventCount = 0;

        for (int i = 0; i < schools.length; i++) {
            if (schools[i].equals("建中")) {
                eventCount++;
            }
        }

        int complementCount = sampleCount - eventCount;
        double probabilityA = (double) eventCount / sampleCount;
        double probabilityComplement = 1 - probabilityA;

        System.out.println("補事件：");
        System.out.println("P(A^c) = 1 - P(A)");
        System.out.println("例如：不是建中的機率");
        System.out.println();

        System.out.println("樣本空間 S：所有學生");
        for (int i = 0; i < names.length; i++) {
            System.out.println(names[i] + "：" + schools[i]);
        }

        System.out.println();
        System.out.println("A：抽到建中學生");
        System.out.println("A^c：抽到不是建中的學生");
        System.out.println("n(S) = " + sampleCount);
        System.out.println("n(A) = " + eventCount);
        System.out.println("n(A^c) = " + complementCount);
        System.out.println("P(A) = " + probabilityA);
        System.out.println("P(A^c) = 1 - " + probabilityA);
        System.out.println("P(A^c) = " + probabilityComplement);
    }
}
