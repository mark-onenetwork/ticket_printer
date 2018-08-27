package au.com.sealink.printing.ticket_printer;

import java.awt.print.PrinterException;
import java.util.List;
import java.util.Map;

import au.com.sealink.printing.ticket_printer.exceptions.NoSuchPrinterException;
import au.com.sealink.printing.ticket_printer.exceptions.NoTicketPageSettingsAssigned;
import au.com.sealink.printing.ticket_printer.ticketfactory.TicketListFactory;
import au.com.sealink.printing.utils.NumberConverter;

public class TicketPrintCommand {

  String printerName = null;
  TicketPageSettings ticketPageSettings;
  List<Ticket> tickets;

  public TicketPrintCommand() {}  

  public void setTicketsFromDataList(List<List<Map<String, Object>>>  ticketData) {
    TicketListFactory ticketListFactory = new TicketListFactory(ticketData);
    this.tickets = ticketListFactory.makeTicketList();
  }

  public void setTickets(List<Ticket> tickets) {
    this.tickets = tickets;
  }

  public void setTicketPageSettingsFromMap(Map<String, Object> pageFormatMap) {
    double width, height, margin;
    width = NumberConverter.objectToDouble(pageFormatMap.get("width"));
    height =  NumberConverter.objectToDouble(pageFormatMap.get("height"));
    margin =  NumberConverter.objectToDouble(pageFormatMap.get("margin"));
    this.ticketPageSettings = new TicketPageSettings(width, height, margin);
  }

  public void setPrinterName(String printerName) {
    this.printerName = printerName;
  }
  
  public void execute() throws PrinterException, NoSuchPrinterException, NoTicketPageSettingsAssigned {
    TicketPrinter ticketPrinter = new TicketPrinter();
    ticketPrinter.setTicketPageSettings(this.ticketPageSettings);
    ticketPrinter.setPrinter(this.printerName);
    ticketPrinter.printTickets(this.tickets);
  }
}