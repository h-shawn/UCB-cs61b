
public class LeapYear {
    public static boolean isLeapYear(int year) {
        return (year % 400 == 0) || (year % 4 == 0 && year % 100 != 0);
    }
    public static void main(String[] args) {
        if(args.length < 1){
            System.out.println("Please input the year to be checked.");
            System.out.println("e.g. java LeapYear 2024.");
        }
        for(int i = 0; i < args.length; i++){
            try {
                int year = Integer.parseInt(args[i]);
                if(isLeapYear(year)){
                    System.out.printf("%d is a leap year.\n", year);
                }else{
                    System.out.printf("%d is not a leap year.\n", year);
                }
            } catch (NumberFormatException e) {
                System.out.printf("%s is not a valid number.\n", args[i]);
            }            
        }
    }
}