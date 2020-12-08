package club.nsuer.nsuer;

public interface CoursesList {
    public static final String[] SUGGESTIONS = {"ACT201", "ACT202", "ACT210", "ACT310", "ACT320", "ACT322", "ACT330", "ACT333", "ACT341", "ACT431", "ANT101", "ARC111", "ARC112", "ARC121", "ARC122", "ARC123", "ARC131", "ARC133", "ARC200", "ARC213", "ARC214", "ARC215", "ARC241", "ARC242", "ARC251", "ARC261", "ARC262", "ARC263", "ARC264", "ARC271", "ARC272", "ARC273", "ARC281", "ARC282", "ARC283", "ARC310", "ARC316", "ARC317", "ARC318", "ARC324", "ARC334", "ARC343", "ARC344", "ARC384", "ARC410", "ARC418", "ARC419", "ARC437", "ARC438", "ARC445", "ARC456", "ARC474", "ARC492", "ARC500", "ARC510", "ARC519", "ARC535", "ARC596", "ARC598", "ATC011", "BBT101", "BBT103", "BBT109", "BBT201", "BBT202", "BBT203", "BBT204", "BBT205", "BBT206", "BBT207", "BBT210", "BBT221", "BBT230", "BBT301", "BBT303", "BBT306", "BBT309", "BBT311", "BBT312", "BBT312L", "BBT313", "BBT314", "BBT314L", "BBT315", "BBT316", "BBT316L", "BBT317", "BBT318", "BBT335", "BBT401", "BBT402", "BBT403", "BBT404", "BBT406", "BBT413", "BBT419", "BBT424", "BBT450", "BBT491", "BBT601", "BBT622", "BBT638", "BBT645", "BBT652", "BBT659", "BBT751", "BEN205", "BIO101", "BIO103", "BIO103L", "BIO201", "BIO201L", "BIO202", "BIO202L", "BUS102", "BUS112", "BUS135", "BUS172", "BUS173", "BUS251", "BUS489", "BUS498", "BUS500", "BUS501", "BUS505", "BUS511", "BUS516", "BUS518", "BUS520", "BUS525", "BUS530", "BUS601", "BUS620", "BUS635", "BUS650", "BUS685", "BUS690", "BUS700", "CEE100", "CEE110", "CEE209", "CEE210", "CEE211", "CEE211L", "CEE212", "CEE213", "CEE213L", "CEE214", "CEE215", "CEE240", "CEE240L", "CEE250", "CEE250L", "CEE260", "CEE310", "CEE330L", "CEE331", "CEE335", "CEE335L", "CEE340", "CEE350", "CEE360", "CEE360L", "CEE373", "CEE373L", "CEE430", "CEE467", "CEE470", "CEE499", "CEG413", "CHE101", "CHE101L", "CHE120", "CHE201", "CHE202", "CHE202L", "CHN101", "CHN201", "CSE115", "CSE115L", "CSE173", "CSE215", "CSE215L", "CSE225", "CSE225L", "CSE231", "CSE231L", "CSE257", "CSE257L", "CSE273", "CSE281", "CSE299", "CSE311", "CSE311L", "CSE323", "CSE325", "CSE326", "CSE327", "CSE331", "CSE331L", "CSE332", "CSE332L", "CSE338", "CSE338L", "CSE373", "CSE410", "CSE413", "CSE413L", "CSE417", "CSE417L", "CSE425", "CSE435", "CSE435L", "CSE440", "CSE470", "CSE473", "CSE482", "CSE482L", "CSE492", "CSE496", "CSE499A", "CSE499B", "CSE542", "CSE545", "CSE552", "CSE564", "CSE573", "CSE598", "DEV503", "DEV564", "DEV565", "DEV567", "DEV570", "DEV595", "DEV596", "ECO101", "ECO103", "ECO104", "ECO134", "ECO172", "ECO173", "ECO201", "ECO203", "ECO204", "ECO244", "ECO260", "ECO303", "ECO309", "ECO314", "ECO315", "ECO328", "ECO349", "ECO372", "ECO406", "ECO430", "ECO455", "ECO465", "ECO472", "ECO495", "ECO496", "ECO499", "ECO501", "ECO503", "ECO512", "ECO570", "ECO651", "ECO682", "ECO689", "ECO695", "ECO699", "EEE111", "EEE111L", "EEE141", "EEE141L", "EEE154", "EEE211", "EEE211L", "EEE221", "EEE221L", "EEE232", "EEE241", "EEE241L", "EEE254", "EEE280", "EEE298", "EEE299", "EEE311", "EEE311L", "EEE312", "EEE312L", "EEE313", "EEE321", "EEE321L", "EEE331", "EEE331L", "EEE332", "EEE332L", "EEE333", "EEE336", "EEE336L", "EEE342", "EEE342L", "EEE361", "EEE362", "EEE362L", "EEE363", "EEE363L", "EEE410", "EEE411", "EEE411L", "EEE413", "EEE413L", "EEE422", "EEE423", "EEE424", "EEE424L", "EEE427", "EEE452", "EEE462", "EEE464", "EEE465", "EEE471", "EEE471L", "EEE481", "EEE482", "EEE494", "EEE499", "EEE499A", "EEE499B", "EEE534", "EEE535", "EEE540", "EEE546", "EEE550", "EEE555", "EEE568", "EMB500", "EMB501", "EMB502", "EMB510", "EMB520", "EMB601", "EMB602", "EMB620", "EMB650", "EMB660", "EMB670", "EMB690", "EMPH611", "EMPH631", "EMPH644", "EMPH711", "EMPH712", "EMPH742", "EMPH805", "EMPH806", "EMPH842", "ENG102", "ENG103", "ENG105", "ENG111", "ENG115", "ENG118", "ENG200", "ENG202", "ENG205", "ENG210", "ENG211", "ENG215", "ENG219", "ENG222", "ENG230", "ENG260", "ENG304", "ENG310", "ENG312", "ENG316", "ENG320", "ENG334", "ENG335", "ENG338", "ENG401", "ENG406", "ENG414", "ENG416", "ENG430", "ENG431", "ENG441", "ENG450", "ENG456", "ENG491", "ENG501", "ENG520", "ENG525", "ENG570", "ENG576", "ENG580", "ENG582", "ENG613", "ENG632", "ENV102", "ENV103", "ENV107", "ENV107L", "ENV172", "ENV203", "ENV204", "ENV205", "ENV207", "ENV208", "ENV214", "ENV215", "ENV303", "ENV307", "ENV311", "ENV315", "ENV316", "ENV373", "ENV408", "ENV412", "ENV414", "ENV416", "ENV419", "ENV430", "ENV455", "ENV495", "ENV498", "ENV499", "ENV501", "ENV502", "ENV602", "ENV605", "ENV624", "ENV627", "ENV630", "ENV631", "ENV642", "ENV668", "ENV690", "ENV697", "ETE111", "ETE111L", "ETE141", "ETE141L", "ETE211", "ETE211L", "ETE221", "ETE221L", "ETE241", "ETE241L", "ETE299", "ETE311", "ETE311L", "ETE312", "ETE312L", "ETE321", "ETE321L", "ETE331", "ETE331L", "ETE332", "ETE332L", "ETE333", "ETE334", "ETE334L", "ETE335", "ETE335L", "ETE361", "ETE411", "ETE412", "ETE412L", "ETE418", "ETE418L", "ETE419", "ETE419L", "ETE422", "ETE423", "ETE424", "ETE424L", "ETE427", "ETE443", "ETE461", "ETE471", "ETE471L", "ETE481", "ETE482", "ETE499A", "ETE499B", "ETE503", "ETE505", "ETE512", "ETE607", "FIN254", "FIN340", "FIN433", "FIN435", "FIN440", "FIN444", "FIN453", "FIN455", "FIN464", "FIN480", "FIN635", "FIN637", "FIN639", "FIN642", "FIN643", "FIN644", "FIN645", "FIN647", "GEO203", "GEO205", "GIO205", "HIS101", "HIS102", "HIS103", "HIS205", "HRM340", "HRM360", "HRM370", "HRM380", "HRM410", "HRM430", "HRM450", "HRM470", "HRM480", "HRM602", "HRM603", "HRM604", "HRM605", "HRM610", "HRM631", "HRM650", "HRM660", "HRM690", "INB350", "INB355", "INB371", "INB372", "INB400", "INB410", "INB480", "INB490", "LAW101", "LAW107", "LAW200", "LAW201", "LAW209", "LAW211", "LAW213", "LAW301", "LAW303", "LAW304", "LAW306", "LAW307", "LAW309", "LAW310", "LAW311", "LAW312", "LAW401", "LAW402", "LAW403", "LAW404", "LAW405", "LAW406", "LAW407", "LAW409", "LAW412", "LAW413", "LBA104", "LLM501", "LLM502", "LLM506", "LLM511", "MAT116", "MAT120", "MAT125", "MAT130", "MAT240", "MAT250", "MAT350", "MAT361", "MAT370", "MGT210", "MGT310", "MGT314", "MGT321", "MGT330", "MGT351", "MGT368", "MGT489", "MGT490", "MGT602", "MGT604", "MGT610", "MGT619", "MGT656", "MGT670", "MGT675", "MIC101", "MIC105", "MIC201", "MIC202", "MIC203", "MIC206", "MIC303", "MIC305", "MIC309", "MIC310", "MIC313", "MIC401", "MIC402", "MIC403", "MIC406", "MIC408", "MIC412", "MIS101", "MIS105", "MIS205", "MIS206", "MIS210", "MIS310", "MIS450", "MIS460", "MIS470", "MIS480", "MIS654", "MIS661", "MKT201", "MKT202", "MKT210", "MKT330", "MKT334", "MKT337", "MKT344", "MKT382", "MKT412", "MKT445", "MKT450", "MKT460", "MKT465", "MKT470", "MKT621", "MKT623", "MKT625", "MKT626", "MKT627", "MKT629", "MKT630", "MKT631", "MKT633", "MKT634", "PAD201", "PBH101", "PBH101L", "PBH107", "PBH605", "PBH611", "PBH631", "PBH642", "PBH643", "PBH644", "PBH653", "PBH663", "PBH671", "PBH672", "PBH678", "PBH681", "PBH701", "PBH704", "PBH706", "PBH711", "PBH712", "PBH713", "PBH714", "PBH731", "PBH734", "PBH742", "PBH781", "PBH782", "PBH805", "PBH806", "PBH842", "PHI101", "PHI104", "PHI401", "PHR100", "PHR110", "PHR111", "PHR112", "PHR113", "PHR113L", "PHR114", "PHR114L", "PHR115", "PHR120", "PHR120L", "PHR121", "PHR122", "PHR122L", "PHR123", "PHR124", "PHR124L", "PHR200", "PHR210", "PHR210L", "PHR211", "PHR211L", "PHR212", "PHR212L", "PHR213", "PHR214", "PHR215", "PHR215L", "PHR220", "PHR221", "PHR221L", "PHR222", "PHR222L", "PHR223", "PHR223L", "PHR224", "PHR224L", "PHR225", "PHR300", "PHR310", "PHR310L", "PHR311", "PHR312", "PHR312L", "PHR313", "PHR313L", "PHR314", "PHR314L", "PHR315", "PHR320", "PHR321", "PHR322", "PHR322L", "PHR323", "PHR324", "PHR325", "PHR400", "PHR410", "PHR411", "PHR411L", "PHR412", "PHR413", "PHR413L", "PHR414", "PHR415", "PHR420", "PHR421", "PHR423", "PHR5001", "PHR5002", "PHR5003", "PHR5011", "PHR5012", "PHR5013", "PHR5015", "PHR5021", "PHR5022", "PHR5023", "PHR5101", "PHR5106", "PHR5107", "PHR5108", "PHR5111", "PHR5112", "PHR5201", "PHR5208", "PHY107", "PHY107L", "PHY108", "PHY108L", "POL101", "POL104", "POL202", "PPG525", "PPG540", "PPG565", "PSY101", "PSY101L", "SCM320", "SCM450", "SOC101", "SOC1O1", "SOC201", "WMS201"
    };
}
