package regmach;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InstructionFactory {

  /*
   * Regexes
   * n: x -> y ----> ^\d:\d->\s*\d$
   * n: x -> y, z ----> ^\d:\d->\d,\d$
   * HALT ----> HALT
   */
  private static final String addPattern = "(^\\d->\\d$)";
  private static final String subPattern = "(^\\d->\\d,\\d$)";
  private static final String haltPattern = "^HALT$";

  private static Pattern addPat = Pattern.compile(addPattern);
  private static Pattern subPat = Pattern.compile(subPattern);
  private static Pattern haltPat = Pattern.compile(haltPattern);
  
  
  public static Instruction getInstruction(RegisterMachine rm, String instruction) {
    
    instruction = instruction.trim().replaceAll("\\s", "");
    
    Matcher addMatcher  = addPat.matcher(instruction);
    Matcher subMatcher  = subPat.matcher(instruction);
    Matcher haltMatcher = haltPat.matcher(instruction);
    
    
    if (addMatcher.find()) {
      String[] parts = instruction.split("->");

      int reg = Integer.parseInt(parts[0]);
      Register register = rm.getRegister(reg);

      int label = Integer.parseInt(parts[1]);

      return new AddInstr(register, label);
    }
    else if (subMatcher.find()) {
      String[] parts = instruction.split("->");
      String[] labels = parts[1].split(",");

      int reg = Integer.parseInt(parts[0]);
      Register register = rm.getRegister(reg);

      // False = able to subtract one True = reg value is zero
      int falseLabel = Integer.parseInt(labels[0]);
      int trueLabel = Integer.parseInt(labels[1]);

      return new SubInstr(register, falseLabel, trueLabel);
    }
    else if (haltMatcher.find()) {
      return new HaltInstr();
    }
    else {
      return new NullInstr();
    }    
  }
}
