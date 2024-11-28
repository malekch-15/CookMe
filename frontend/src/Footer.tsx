import "./Footer.css"

export default function Footer() {
    return (
        <footer className="footer">
            <div className="footer-container">
                <div className="footer-logo">
                    <img src="logo.png" alt="Logo"/>
                    <p>Crafting your meals with care.</p>
                </div>
                <div className="footer-links">
                    <a>About Us</a>
                    <a>Contact</a>
                    <a>Privacy Policy</a>
                    <a>Terms of Service</a>
                </div>
                <div className="footer-socials">
                    <a href="https://facebook.com" target="_blank" rel="noopener noreferrer">
                        Facebook
                    </a>
                    <a href="https://twitter.com" target="_blank" rel="noopener noreferrer">
                        Twitter
                    </a>
                    <a href="https://instagram.com" target="_blank" rel="noopener noreferrer">
                        Instagram
                    </a>
                </div>
            </div>
            <div className="footer-bottom">
                <p>&copy; {new Date().getFullYear()} CookMe. All rights reserved.</p>
            </div>
        </footer>
    );

}