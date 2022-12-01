package application.frontend;

public class WinnerChecker {
    public static char winner(char[][] cb){
        for (int i = 0; i < 3; i++){
            if(cb[i][0] == cb[i][1] && cb[i][1] == cb[i][2] && cb[i][0] != '\0'){
                return cb[i][0];
            }

            if(cb[0][i] == cb[1][i] && cb[1][i] == cb[2][i] && cb[0][i] != '\0'){
                return cb[0][i];
            }
        }

        if(cb[0][0] == cb[1][1] && cb[1][1] == cb[2][2] && cb[1][1] != '\0'){
            return cb[1][1];
        }else if(cb[0][2] == cb[1][1] && cb[1][1] == cb[2][0] && cb[1][1] != '\0'){
            return cb[1][1];
        }


        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if(cb[i][j] == '\0'){
                    return 'N';
                }
            }
        }

        System.out.println(1);

        return 'F';
    }

    public static char[][] loadChessboard(String all){
        char[][] cb = new char[3][3];
        String[] ss = all.split("\n");
        for (int i = 0; i < 3; i++) {
            String[] s2 = ss[i].split(" ");
            for (int j = 0; j < 3; j++) {
                cb[i][j] = s2[j].charAt(0);
            }
        }

        return cb;
    }

    private static char loadWinner(String all){
        return all.split("\n")[3].charAt(0);
    }
}
