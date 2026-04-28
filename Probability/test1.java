class test1 {
    public static void main(String[] args) {
        String[] sampleSpace = {"小明", "小華", "小美", "小英"};

        System.out.println("樣本空間（Sample Space）：");
        System.out.println("S：所有可能結果的集合");
        System.out.println("例如：抽一位學生，所有學生就是樣本空間");
        System.out.println();

        System.out.println("S = 所有學生");
        System.out.print("S = { ");

        for (int i = 0; i < sampleSpace.length; i++) {
            System.out.print(sampleSpace[i]);

            if (i < sampleSpace.length - 1) {
                System.out.print(", ");
            }
        }

        System.out.println(" }");
    }
}
