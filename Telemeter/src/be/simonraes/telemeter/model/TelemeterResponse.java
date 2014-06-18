package be.simonraes.telemeter.model;

/**
 * Created by Simon Raes on 13/06/2014.
 */
public class TelemeterResponse {

    //always:
    private Ticket ticket;
    //Fup
    private Period period;
    private Usage usage;
    private String status;
    private StatusDescription statusDescription;
    //Volume
    private Volume volume;
    //in case of error:
    private Fault fault;

    public Ticket getTicket() {
        if (ticket != null) {
            return ticket;
        } else {
            return new Ticket();
        }
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public Period getPeriod() {
        if (period != null) {
            return period;
        } else {
            return new Period();
        }
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public Usage getUsage() {
        if (usage != null) {
            return usage;
        } else {
            return new Usage();
        }
    }

    public void setUsage(Usage usage) {
        this.usage = usage;
    }

    public String getStatus() {
        if (status != null) {
            return status;
        } else {
            return "";
        }
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public StatusDescription getStatusDescription() {
        if (statusDescription != null) {
            return statusDescription;
        } else {
            return new StatusDescription();
        }
    }

    public void setStatusDescription(StatusDescription statusDescription) {
        this.statusDescription = statusDescription;
    }

    public Volume getVolume() {
        if (volume != null) {
            return volume;
        } else {
            return new Volume();
        }
    }

    public void setVolume(Volume volume) {
        this.volume = volume;
    }

    public Fault getFault() {
        if (fault != null) {
            return fault;
        } else {
            return new Fault();
        }
    }

    public void setFault(Fault fault) {
        this.fault = fault;
    }

    /*
    Volume response:

    <S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ns2="http://www.telenet.be/TelemeterService/">
       <S:Body>
          <ns2:RetrieveUsageResponse>
             <Ticket>
                <Timestamp>2011-06-27T12:53:31.617+02:00</Timestamp>
                <ExpiryTimestamp>2011-06-27T13:56:56.617+02:00</ExpiryTimestamp>
             </Ticket>
             <Volume>
                <Unit>MB</Unit>
                <Limit>10000</Limit>
                <TotalUsage>1356</TotalUsage>
                <DailyUsageList>
                   <DailyUsage>
                      <Day>2011-06-15+02:00</Day>
                      <Usage>223</Usage>
                   </DailyUsage>
                   <DailyUsage>
                      <Day>2011-06-16+02:00</Day>
                      <Usage>863</Usage>
                   </DailyUsage>
                   <DailyUsage>
                      <Day>2011-06-17+02:00</Day>
                      <Usage>270</Usage>
                   </DailyUsage>
                </DailyUsageList>
             </Volume>
          </ns2:RetrieveUsageResponse>
       </S:Body>
    </S:Envelope>


    FUP response:

     <?xml version='1.0' encoding='UTF-8'?>
    <S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">
    <S:Body>
    <ns2:RetrieveUsageResponse xmlns:ns2="http://www.telenet.be/TelemeterService/">
    <Ticket>
        <Timestamp>2014-06-08T21:41:39.059+02:00</Timestamp>
        <ExpiryTimestamp>2014-06-08T22:46:59.059+02:00</ExpiryTimestamp>
    </Ticket>
    <FUP>

        <Period>
            <From>2014-05-16+02:00</From>
            <Till>2014-06-15+02:00</Till>
            <CurrentDay>24</CurrentDay>
        </Period>

        <Usage>
            <TotalUsage>151.72</TotalUsage>
            <MinUsageRemaining>344.23</MinUsageRemaining>
            <MaxUsageRemaining>1114.3</MaxUsageRemaining>
            <Unit>GB</Unit>
            <LastUpdate>2014-06-08T21:30:49.000+02:00</LastUpdate>
        </Usage>

        <Status>Vrij verbruik</Status>

        <StatusDescription>
            <NL>Je surft met volledige surfsnelheid.</NL>
            <FR>Vous surfez à vitesse normale.</FR>
        </StatusDescription>

    </FUP>
    </ns2:RetrieveUsageResponse>
    </S:Body>
    </S:Envelope>
            * */



    /*Error response:

    Error for wrong login credentials:

    <?xml version='1.0' encoding='UTF-8'?>
    <S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">
        <S:Body>
            <S:Fault xmlns:ns4="http://www.w3.org/2003/05/soap-envelope">
        <faultcode>S:Server</faultcode>
        <faultstring>Exception ID: wlsCbeCusSlfWebD1_tlmtls_tele057.corp.telenet.be_1403019177799
        Error Messages: ERRTLMTLS_00004:tlmtls:Authentication Failed for Login - x.
        Incorrect Login or Password specified.</faultstring>
                <detail>
                    <ns2:TelemeterFault xmlns:ns2="http://www.telenet.be/TelemeterService/">
                        <Code>ERRTLMTLS_00001</Code>
                        <Description>Exception ID: wlsCbeCusSlfWebD1_tlmtls_tele057.corp.telenet.be_1403019177799
                        Error Messages: ERRTLMTLS_00004:tlmtls:Authentication Failed for Login - x. Incorrect Login or Password specified.</Description>
                    </ns2:TelemeterFault>
                </detail>
            </S:Fault>
        </S:Body>
    </S:Envelope>

    Error for refreshing too often:

    <?xml version='1.0' encoding='UTF-8'?>
    <S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">
    <S:Body>
        <S:Fault xmlns:ns4="http://www.w3.org/2003/05/soap-envelope">
            <faultcode>S:Server</faultcode>
            <faultstring>Exception ID: wlsCbeCusSlfWebA1_tlmtls_tele039.corp.telenet.be_1402676094907
            Error Messages: ERRTLMTLS_00003:tlmtls:Login - t1234567 not authorized to get Telemeter usage.
            Please try accessing data after expiry time - 13/06/14 18:55.
            </faultstring>
            <detail>
                <ns2:TelemeterFault xmlns:ns2="http://www.telenet.be/TelemeterService/">
                    <Code>ERRTLMTLS_00001</Code>
                    <Description>Exception ID: wlsCbeCusSlfWebA1_tlmtls_tele039.corp.telenet.be_1402676094907
                    Error Messages: 	ERRTLMTLS_00003:tlmtls:Login - t1234567 not authorized to get Telemeter usage.
                    Please try accessing data after expiry time - 13/06/14 18:55.
                    </Description>
                </ns2:TelemeterFault>
            </detail>
        </S:Fault>
    </S:Body>
    </S:Envelope>
    * */
}
