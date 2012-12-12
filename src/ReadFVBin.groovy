import groovy.transform.TypeChecked

import java.nio.ByteBuffer

import static java.nio.ByteOrder.LITTLE_ENDIAN

@TypeChecked
class ReadFVBin {
    final static int f_hand_pawn = 0,
                     e_hand_pawn = 19,
                     f_hand_lance = 38,
                     e_hand_lance = 43,
                     f_hand_knight = 48,
                     e_hand_knight = 53,
                     f_hand_silver = 58,
                     e_hand_silver = 63,
                     f_hand_gold = 68,
                     e_hand_gold = 73,
                     f_hand_bishop = 78,
                     e_hand_bishop = 81,
                     f_hand_rook = 84,
                     e_hand_rook = 87,
                     fe_hand_end = 90,
                     f_pawn = 81,
                     e_pawn = 162,
                     f_lance = 225,
                     e_lance = 306,
                     f_knight = 360,
                     e_knight = 441,
                     f_silver = 504,
                     e_silver = 585,
                     f_gold = 666,
                     e_gold = 747,
                     f_bishop = 828,
                     e_bishop = 909,
                     f_horse = 990,
                     e_horse = 1071,
                     f_rook = 1152,
                     e_rook = 1233,
                     f_dragon = 1314,
                     e_dragon = 1395,
                     fe_end = 1476

    final static int nsquare = 9 * 9
    final static int pos_n = fe_end * (fe_end + 1) / 2 as int
    final static int kkpStartPos = nsquare * pos_n

    final ByteBuffer fv = ByteBuffer.wrap(new File("../bonanza_data/fv.bin").bytes).order(LITTLE_ENDIAN)

    void testKin() {
        // 28王、38銀、49金（美濃囲い）
        int ou = toIdx(2, 8)
        int gin = toIdx(3, 8)
        for (int kin = 0; kin < 81; kin++) {
            // println "${toXY(ou)}王, ${toXY(gin)}銀, ${toXY(kin)}金, value = ${toValue(ou, gin, kin)}"

            print "${toValue(ou, gin, kin)}\t"
            if (kin % 9 == 8) println()
        }
    }

    void testGin() {
        int ou = toIdx(2, 8)
        int kin = toIdx(4, 9)
        for (int gin = 0; gin < 81; gin++) {
            // println "${toXY(ou)}王, ${toXY(gin)}銀, ${toXY(kin)}金, value = ${toValue(ou, gin, kin)}"

            print "${toValue(ou, gin, kin)}\t"
            if (gin % 9 == 8) println()
        }
    }

    void testOu() {
        int kin = toIdx(4, 9)
        int gin = toIdx(3, 8)
        for (int ou = 0; ou < 81; ou++) {
            // println "${toXY(ou)}王, ${toXY(gin)}銀, ${toXY(kin)}金, value = ${toValue(ou, gin, kin)}"

            print "${toValue(ou, gin, kin)}\t"
            if (ou % 9 == 8) println()
        }
    }

    void testOu2() {
        // 右の金銀が初期状態
        int kin = toIdx(4, 9)
        int gin = toIdx(3, 9)
        for (int ou = 0; ou < 81; ou++) {
            // println "${toXY(ou)}王, ${toXY(gin)}銀, ${toXY(kin)}金, value = ${toValue(ou, gin, kin)}"

            print "${toValue(ou, gin, kin)}\t"
            if (ou % 9 == 8) println()
        }
    }

    void testFindMax() {
        int ou = toIdx(2, 8)

        def values = []
        for (int gin = 0; gin < 81; gin++) {
            for (int kin = 0; kin < 81; kin++) {
                values << [gin, kin, toValue(ou, gin, kin)]
            }
        }
        values.sort { -(it[2] as int) }

        for (def v in values) {
            println "${toXY(ou)}王, ${toXY(v[0] as int)}銀, ${toXY(v[1] as int)}金, value = ${v[2]}"
        }
    }

    short toValue(int ou, int gin, int kin) {
        assert f_gold >= f_silver
        int pos = pos_n * ou + toPcOnSqPos(f_gold + kin) + (f_silver + gin)
        return fv.getShort(pos * 2)
    }

    int toPcOnSqPos(int i) { i * (i + 1) / 2 as int }
    int toX(int i) { 9 - (i % 9 as int) }
    int toY(int i) { (i / 9 as int) + 1 }
    int toIdx(int x, int y) { (9 - x) + (y - 1) * 9 }
    String toXY(int i) { "${toX(i)}${toY(i)}" }

    public static void main(String[] args) {
        def t = new ReadFVBin()
//        t.testKin()
        t.testGin()
//        t.testOu()
//        t.testOu2()
//        t.testFindMax()
    }
}
