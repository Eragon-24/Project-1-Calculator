import static java.lang.Math.exp;

public class Advanced_operations {
        public static double power(double a, int b) {

            return Math.pow(a,b);
        }

        public static double log(double a)
        {
            return Math.log10(a);
        }

        public static double ln(double a)
        {
            return Math.log(a);
        }

        public static double sqrroot(double a)
        {
            if (a<0) {
                throw new IllegalArgumentException("Cannot use negative numbers");
            }
            return Math.sqrt(a);
        }
        public static double percent(double a,double b)
        {
            return (a*b)/100.0;
        }
        public static double add_percentage(double a,double b)
        {
            return a+(a*b)/100.0;
        }
        public static double expo(int a)
        {
            return exp(a);
        }
    }

