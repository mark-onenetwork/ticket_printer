package com.quicktravel.ticket_printer;

import java.awt.print.PrinterJob;

import javax.print.PrintService;

import com.quicktravel.ticket_printer.exceptions.NoSuchPrinterException;

public class PrintServiceLocator {

  public PrintServiceLocator() {}

  /*
   * Returns list of printers (as PrintService objects)
   */
  public PrintService[] getAll() {
    return PrinterJob.lookupPrintServices();
  }

  public PrintService findByIndex(int index) throws NoSuchPrinterException {
    if (index >= getAll().length) {
      throw new NoSuchPrinterException();
    }
    
    return getAll()[index];
  }
  
  public PrintService findByName(String name)  throws NoSuchPrinterException {
    for (PrintService printService : getAll()) {
      if (printService.getName().equals(name)) {
        return printService;
      }
    }

    // couldn't find it...
    throw new NoSuchPrinterException();    
  }

  public void logAvailablePrinters() {
    System.out.println("PrinterJobAdapter#logAvailablePrinters()");
    for (PrintService printService : this.getAll()) {
      System.out.println(printService);
    }
    System.out.println("Done.");
  }
}
