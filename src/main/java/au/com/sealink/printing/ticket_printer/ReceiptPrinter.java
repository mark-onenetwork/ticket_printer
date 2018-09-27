package au.com.sealink.printing.ticket_printer;

import au.com.sealink.printing.receipt.CutMode;
import au.com.sealink.printing.receipt.EpsonPrinter;
import au.com.sealink.printing.ticket_printer.exceptions.NoSuchPrinterException;

import javax.print.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ReceiptPrinter {
    private final PrintService printer;
    private static final Object ticketPrintingLock = new Object();

    public ReceiptPrinter(String name) throws NoSuchPrinterException {
        this.printer = new PrintServiceLocator().findByName(name);
    }

    public void printTickets(Iterable<Ticket> tickets) throws IOException, PrintException {
        for (Ticket ticket : tickets) {
            printTicket(ticket);
        }
    }

    public void printTicket(Ticket ticket) throws IOException, PrintException {
        try (ByteArrayOutputStream bs = new ByteArrayOutputStream()) {
            try (EpsonPrinter printer = new EpsonPrinter(bs)) {
                printer.initialise();
                for (TicketElement element : ticket.getElements()) {
                    printer.setEmphasis(element.isBold());
                    printer.setUnderline(element.getUnderline());
                    printer.setJustification(element.getJustification());
                    printer.text(element.getValue());
                    printer.text(System.lineSeparator());
                }
                printer.feed(3);
                printer.cut(CutMode.FULL);

                synchronized (ticketPrintingLock) {
                    Doc doc = new SimpleDoc(bs.toByteArray(), DocFlavor.BYTE_ARRAY.AUTOSENSE, null);
                    DocPrintJob job = this.printer.createPrintJob();
                    job.print(doc, null);
                }
            }
        }
    }
}
