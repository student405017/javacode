class test2 {
    public static void main(String[] args) {
        String[] names = {"小明", "小華", "小美", "小英"};
        String[] schools = {"建中", "北一女", "建中", "成功高中"};

        System.out.println("事件（Event）：");
        System.out.println("A：我們關心的事件");
        System.out.println("例如：抽到建中學生");
        System.out.println();

        System.out.println("樣本空間 S：所有學生");
        for (int i = 0; i < names.length; i++) {
            System.out.println(names[i] + "：" + schools[i]);
        }

        System.out.println();
        System.out.println("事件 A：抽到建中學生");
        System.out.print("A = { ");

        boolean first = true;
        for (int i = 0; i < names.length; i++) {
            if (schools[i].equals("建中")) {
                if (!first) {
                    System.out.print(", ");
                }

                System.out.print(names[i]);
                first = false;
            }
        }

        System.out.println(" }");
    }
}
