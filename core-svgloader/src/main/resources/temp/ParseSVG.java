package temp;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import org.apache.batik.parser.LengthParser;
import org.apache.batik.parser.PathParser;
import org.apache.batik.parser.TransformListParser;


import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;



public class ParseSVG {
	public final String TAG = this.getClass().getSimpleName();
		
	public AffineTransformation at = new AffineTransformation();
	
	public ArrayList<Spline> paths = new ArrayList<Spline>();
	
	private String URI;
	
	public ParseSVG(String path)  {
		URI = path;
		
		
		ResourceManager res = new ResourceManager();
				
		Document dom;
		try {
			dom = res.loadXmlFile(path);
			Element baseElement = dom.getDocumentElement();
			
			// parse attributes from the SVG tag
			parseSVG(baseElement);
		
			// Parse global transforms
			parseGlobalTransform(baseElement);
			
			// Parse paths
			parsePaths(baseElement);


		} catch (IOException e) {
			e.printStackTrace();
		}
	
	
		

	}
	
	public void parseSVG (Element baseElement) {
		
		LengthParser lp = new LengthParser();
		LengthHandler lh = new LengthHandler();
		
		lp.setLengthHandler(lh);
		
		lp.parse(baseElement.getAttribute("width"));
		
		//float width = lh.value;
		
		lp.parse(baseElement.getAttribute("height"));
		
		float height = lh.value;
		
		
		
		
		//at.translate(0, height);
		at.scale(1,-1);
		at.translate(0, height);
		
	}
	
	public void parseGlobalTransform(Element baseElement) {
		// Get graphics transform
		NodeList nl = baseElement.getElementsByTagName("g");

		Element gTag = (Element) nl.item(0);
		String transform = gTag.getAttribute("transform");
		
		at.composeBefore(parseTransform(transform));
	}
	
	private AffineTransformation parseTransform (String transform) {
		// Parse
		TransformHandler th = new TransformHandler();
		TransformListParser tlp = new TransformListParser();
		
		tlp.setTransformListHandler(th);
		tlp.parse(transform);
		
		//at.composeBefore(th.at);
		return th.at;
	}
	
	public void parsePaths(Element baseElement) {
		
		NodeList nl = baseElement.getElementsByTagName("path");
		
		// Path parser
		OpenGLPathHandler ph = new OpenGLPathHandler();
		PathParser pp = new PathParser();
		pp.setPathHandler(ph);
				
		for(int i=0; i<nl.getLength(); i++) {
			Element elm = (Element) nl.item(i);

			// Get path data
			String path = elm.getAttribute("d");
			LogMessage.print(path);
			pp.parse(path);
			
			// Get transformation data
			String transform = elm.getAttribute("transform");
			
			// Transform the resulting spline
			ph.currentSpline.transfrom(parseTransform(transform));
			addPath(ph.currentSpline);
		}
 	}
	
	public void toPNG () throws TranscoderException, IOException {
        // Create a JPEG transcoder
        PNGTranscoder t = new PNGTranscoder();

        // Set the transcoding hints.
        //t.addTranscodingHint(PNGTranscoder.KEY_QUALITY,
        //                     new Float(.8));

        // Create the transcoder input.
        //String svgURI = new File(args[0]).toURL().toString();
        TranscoderInput input = new TranscoderInput(URI);

        // Create the transcoder output.
        OutputStream ostream;
			ostream = new FileOutputStream("src/res/test.png");
	        TranscoderOutput output = new TranscoderOutput(ostream);

	        // Save the image.
	        t.transcode(input, output);

	        // Flush and close the stream.
	        ostream.flush();
	        ostream.close();
	        //System.exit(0);

	}

	
	public void addPath(Spline s) {
		// Apply global transform
		s.transfrom(at);
		paths.add(s);
	}
	
	

	

	

}
