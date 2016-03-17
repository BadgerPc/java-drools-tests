package pl.speednet.drools;

import java.util.Map;

import org.apache.log4j.BasicConfigurator;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.event.rule.AfterActivationFiredEvent;
import org.drools.event.rule.BeforeActivationFiredEvent;
import org.drools.event.rule.DefaultAgendaEventListener;
import org.drools.event.rule.ObjectInsertedEvent;
import org.drools.event.rule.ObjectRetractedEvent;
import org.drools.event.rule.ObjectUpdatedEvent;
import org.drools.event.rule.WorkingMemoryEventListener;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pl.speednet.drools.api.DroolsJBossLogger;
import pl.speednet.drools.api.SimpleFact;
import pl.speednet.drools.dto.Messages;


/**
 * This is a sample file to launch a process.
 */
public class DroolsTests {
	
	private static final Logger log = LoggerFactory.getLogger(DroolsTests.class);

    public static final void main(String[] args) {
     	
    	BasicConfigurator.configure();
     	
    	runWorkFlowExample();
    
    }
    
    private static void runWorkFlowExample() {
    	
    	try {

    		KnowledgeBase kbase = readKnowledgeBase("WorkFlowExample", "workFlowExample");
            StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
            
            handleLogBeforeRulesFired(ksession);
            
            Messages message = new Messages();
            
            ksession.insert(message);
            ksession.startProcess("pl.speednet.drools", null);
            ksession.fireAllRules();

    	} catch (Throwable t) {
            t.printStackTrace();
        }
    }
    
    
    private static KnowledgeBase readKnowledgeBase(String drlName, String bpmnName) throws Exception {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        kbuilder.add(ResourceFactory.newClassPathResource("process/" + drlName + ".drl"), ResourceType.DRL);
        kbuilder.add(ResourceFactory.newClassPathResource("process/" + bpmnName + ".bpmn"), ResourceType.BPMN2);
        KnowledgeBuilderErrors errors = kbuilder.getErrors();
        if (errors.size() > 0) {
            for (KnowledgeBuilderError error: errors) {
                System.err.println(error);
            }
            throw new IllegalArgumentException("Could not parse knowledge.");
        }
        KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
        return kbase;
    }
    
    private static void handleLogBeforeRulesFired(StatefulKnowledgeSession kSession) {
    	
    	kSession.addEventListener(( new DefaultAgendaEventListener() {
			
			private long timer;
		
			@Override
			public void beforeActivationFired(BeforeActivationFiredEvent event) {
				super.beforeActivationFired(event);
				log.info("-----------------------------------------------");
				log.info("Rule : " + event.getActivation().getRule().getName());
				org.drools.spi.Activation activation = (org.drools.spi.Activation) event.getActivation();
				log.info("Salience : " + activation.getSalience());
				if (activation.getRule().getRuleFlowGroup() != null) {
					log.info("Ruleflow group : " + activation.getRule().getRuleFlowGroup());
				}
				Map<String, Object> metaData = activation.getRule().getMetaData();
				if (metaData != null) {
					for (Map.Entry<String, Object> entry : metaData.entrySet()) {
						log.info("Rule attribute " + entry.getKey() + " : " + entry.getValue());
					}
				}
				
				this.timer = System.currentTimeMillis();
				
			}
			@Override
			public void afterActivationFired(AfterActivationFiredEvent event) {
				super.afterActivationFired(event);
				log.info("Execution time : " + (System.currentTimeMillis() - timer) + " ms");
			}
		})); 
    	
		kSession.addEventListener(new WorkingMemoryEventListener() {
			
			public void objectUpdated(ObjectUpdatedEvent event) {
				if (event.getObject() instanceof SimpleFact) {
					SimpleFact sf = (SimpleFact) event.getObject();
					log.info(" * FACT UPDATED (name : '" + sf.getName() + ")");
				}
			}
		
			public void objectRetracted(ObjectRetractedEvent event) {
				if (event.getOldObject() instanceof SimpleFact) {
					SimpleFact sf = (SimpleFact) event.getOldObject();
					log.info(" * FACT RETRACTED (name : '" + sf.getName() + ")");
				}
			}
		
			public void objectInserted(ObjectInsertedEvent event) {
				if (event.getObject() instanceof SimpleFact) {
					SimpleFact sf = (SimpleFact) event.getObject();
					log.info(" * FACT INSERTED (name : '" + sf.getName() + "', value : " + sf.getStringValue() + ")");
				}
			}
		});
		
		kSession.setGlobal("LOG", new DroolsJBossLogger(log));
    }

}
