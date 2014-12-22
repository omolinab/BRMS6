package org.drools.bug.leak;

import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;
import sun.tools.attach.HotSpotVirtualMachine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

public class HeapHisto {

    public static String getHisto() {
        String histo = "";
        try {
            String vmid = getOwnPid();
            InputStream is = makeHisto(vmid);
            histo = getText(new BufferedReader(new InputStreamReader(is)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return histo;
    }
    public static InputStream makeHisto(String vmid) throws IOException, AttachNotSupportedException {
        HotSpotVirtualMachine vm = (HotSpotVirtualMachine) VirtualMachine.attach(vmid);
        InputStream histo = vm.heapHisto(new Object[] {"-live"});
        vm.detach();
        return histo;
    }
    public static String getOwnPid() {
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        String name = runtime.getName();
        return name.substring(0, name.indexOf('@'));
    }
    public static String getText(BufferedReader reader) throws IOException {
        StringBuilder answer = new StringBuilder();
        char[] charBuffer = new char[8192];
        int nbCharRead;
        while ((nbCharRead = reader.read(charBuffer)) != -1) {
            answer.append(charBuffer, 0, nbCharRead);
        }
        reader.close();
        return answer.toString();
    }
}

