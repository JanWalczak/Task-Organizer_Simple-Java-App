
public enum BasePriorityChoice {

        High(1.0),
        Medium(1.5),
        Low(2.0);

        double baseValue;

        private BasePriorityChoice(double value) {
            baseValue = value;
        }

        public double getBaseValue(){
                return baseValue;
        }


}

