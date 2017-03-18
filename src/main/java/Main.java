import javafx.scene.control.ScrollPane;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.util.Collections.swap;

public class Main {
    private static List<int[]> arrPerm;

    public static void main(String[] args) {
        // для начала, необходимо перебрать число всевозможных перестановок от 1 до n

        // перестановка вида (1, 2, 3, 4)
        //                   (4, 3, 2, 1)
        // образует "матрицу перестановки"

        // 0 0 0 1
        // 0 0 1 0
        // 0 1 0 0
        // 1 0 0 0 +

        // графы изоморфны, если их матрицы перестановочно подобны
        // то есть: PAP^-1 = B => нужна обратная матрица +
        // и произведение матриц +

        Scanner scan = new Scanner(System.in);
        System.out.println("Enter the number of graphs: ");
        int number = scan.nextInt();

        System.out.println("Enter the size of graphs matrix: ");
        int size = scan.nextInt();

        int[][][] adjMatrix = new int[number][size][size];

        // input
        for (int i = 0; i < number; i++) {
            System.out.println("Fill in matrix #" + (i + 1) + ": ");

            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    if (i > 0) {
                        adjMatrix[i][x][y] = scan.nextInt();
                    } else {
                        adjMatrix[i][x][y] = scan.nextInt();
                    }
                }
            }
        }

        // generating the first permutation
        int[] arr = new int[size];
        for (int i = 1; i < size; i++) arr[i] = i;

        // generating others
        arrPerm = new ArrayList<>();
        permutations(0, arr.clone());

        boolean iso = false;
        List<Pair> isoList = new ArrayList<>();
        for(int n1 = 0; n1 < number; n1++) {
            for(int n2 = n1 + 1; n2 < number; n2++) {
                for (int[] arr2 : arrPerm) {
                    // generating similar matrix P
                    int[][] matrixP = new int[arr.length][arr.length];
                    for (int i = 0; i < arr.length; i++) {
                        matrixP[arr[i]][arr2[i]] = 1;
                    }

                    /* for(int[] a : matrixP) {
                        for (int i : a) System.out.print(i + " ");
                        System.out.println("\r");
                    }

                    System.out.println(" ->>>>>>>>>> "); */

                    // P - ортогональная матрица, её обратная = траспонированной (thanks God)
                    int[][] inverseP = getInverse(matrixP);
                    matrixP = matrixMultiply(matrixP, adjMatrix[n1]);
                    matrixP = matrixMultiply(matrixP, inverseP);

                    if (matrixEquals(matrixP, adjMatrix[n2])) {
                        iso = true;
                        break;
                    }
                }

                if (iso) {
                    isoList.add(new Pair(n1, n2));
                    iso = false;
                }
            }
        }

        if(isoList.size() > 0) {
            System.out.println("We found isomorphic pairs: ");
            for (Pair pair : isoList) {
                System.out.print(" {" + pair.getA() + ", " + pair.getB() + "}");
            }
        }else{
            System.out.println("This graphs are not isomorphic");
        }
    }

    // just swap
    private static int[] swapped(int[] arr, int i, int j){
        if(i == j) return arr;

        arr[j] ^= (arr[i] ^= arr[j]);
        arr[i] ^= arr[j];
        return arr.clone();
    }

    private static void permutations(int iter, int[] arr) {
        if (iter >= arr.length - 1) {
            arrPerm.add(arr);
            //for(int i : arr) System.out.print(i + " ");
            //System.out.println("\r");
        } else {
            for (int i = iter; i < arr.length; i++) {
                permutations(iter + 1, swapped(arr, iter, i));
            }
        }
    }

    private static int[][] getInverse(int[][] base) {
        int[][] inverse = new int[base.length][base.length];
        for (int i = 0; i < base.length; i++) {
            for (int j = 0; j < base.length; j++) {
                inverse[i][j] = base[j][i];
            }
        }

        return inverse;
    }

    private static int[][] matrixMultiply(int[][] m1, int[][] m2) {
        int[][] res = new int[m1.length][m1.length];

        for (int s = 0; s < m1.length; s++) {
            for (int c = 0; c < m1.length; c++) {
                for (int p = 0; p < m1.length; p++) {
                    res[s][c] += m1[s][p] * m2[p][c];
                }
            }
        }

        return res;
    }

    private static boolean matrixEquals(int[][] m1, int[][] m2) {
        for (int i = 0; i < m1.length; i++) {
            for (int j = 0; j < m2.length; j++) {
                if (m1[i][j] != m2[i][j]) return false;
            }
        }

        return true;
    }
}
